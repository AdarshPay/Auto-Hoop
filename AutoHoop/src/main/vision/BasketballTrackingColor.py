# import the necessary packages
from collections import deque
from imutils.video import VideoStream
import numpy as np
import argparse
import cv2
import imutils
import time
# construct the argument parse and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-v", "--video",
	help="path to the (optional) video file")
ap.add_argument("-b", "--buffer", type=int, default=64,
	help="max buffer size")
args = vars(ap.parse_args())

# define the lower and upper boundaries of the "green"
# ball in the HSV color space, then initialize the
# list of tracked points
pts = deque(maxlen=args["buffer"])
# if a video path was not supplied, grab the reference
# to the webcam
if not args.get("video", False):
	vs = VideoStream(src=0).start()
# otherwise, grab a reference to the video file
else:
	vs = cv2.VideoCapture(args["video"])
	vs.set(cv2.CAP_PROP_EXPOSURE, 40)
# allow the camera or video file to warm up
time.sleep(2.0)

def on_change(value):
    print(value)

lowerH = 10
upperH = 25

lowerS = 100
upperS = 255

lowerV = 20
upperV = 200

lowerHSV = (lowerH, lowerS, lowerV)
upperHSV = (upperH, upperS, lowerV)

cv2.namedWindow('HSV Tuner', cv2.WINDOW_AUTOSIZE)

cv2.createTrackbar('Lower H', "HSV Tuner", 0, 255, on_change)
cv2.createTrackbar('Higher H', "HSV Tuner", 0, 255, on_change)
cv2.createTrackbar('Lower S', "HSV Tuner", 0, 255, on_change)
cv2.createTrackbar('Higher S', "HSV Tuner", 0, 255, on_change)
cv2.createTrackbar('Lower V', "HSV Tuner", 0, 255, on_change)
cv2.createTrackbar('Higher V', "HSV Tuner", 0, 255, on_change)

cv2.setTrackbarPos('Lower H', "HSV Tuner", lowerH)
cv2.setTrackbarPos('Higher H', "HSV Tuner", upperH)
cv2.setTrackbarPos('Lower S', "HSV Tuner", lowerS)
cv2.setTrackbarPos('Higher S', "HSV Tuner", upperS)
cv2.setTrackbarPos('Lower V', "HSV Tuner", lowerV)
cv2.setTrackbarPos('Higher V', "HSV Tuner", upperV)

# keep looping
while True:
	lowerH = cv2.getTrackbarPos('Lower H', "HSV Tuner")
	upperH = cv2.getTrackbarPos('Higher H', "HSV Tuner")

	lowerS = cv2.getTrackbarPos('Lower S', "HSV Tuner")
	upperS = cv2.getTrackbarPos('Higher S', "HSV Tuner")

	lowerV = cv2.getTrackbarPos('Lower V', "HSV Tuner")
	upperV = cv2.getTrackbarPos('Higher V', "HSV Tuner")
	# print(lowerH)

	# grab the current frame
	frame = vs.read()
	# handle the frame from VideoCapture or VideoStream
	frame = frame[1] if args.get("video", False) else frame
	# if we are viewing a video and we did not grab a frame,
	# then we have reached the end of the video
	if frame is None:
		break
	# resize the frame, blur it, and convert it to the HSV
	# color space
	frame = imutils.resize(frame, width=600)
	blurred = cv2.GaussianBlur(frame, (11, 11), 0)
	hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
	hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
	lowerThreshold = np.array([lowerH, lowerS, lowerV])
	upperThreshold = np.array([upperH, upperS, upperV])

	#check if color in range
	mask = cv2.inRange(hsv, lowerThreshold, upperThreshold)
	# blur = cv2.GaussianBlur(mask, (10, 10), cv2.BORDER_DEFAULT)

	# mask = cv2.erode(mask, None, iterations=2)
	mask = cv2.dilate(mask, None, iterations=2)
	result = cv2.bitwise_and(frame, frame, mask = mask)


    # find contours in the mask and initialize the current
	# (x, y) center of the ball
	cnts = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL,
		cv2.CHAIN_APPROX_SIMPLE)
	cnts = imutils.grab_contours(cnts)
	center = None
	# only proceed if at least one contour was found
	if len(cnts) > 0:
		# find the largest contour in the mask, then use
		# it to compute the minimum enclosing circle and
		# centroid
		c = max(cnts, key=cv2.contourArea)
		((x, y), radius) = cv2.minEnclosingCircle(c)
		M = cv2.moments(c)
		center = (int(M["m10"] / M["m00"]), int(M["m01"] / M["m00"]))
		# only proceed if the radius meets a minimum size
		if radius > 10:
			# draw the circle and centroid on the frame,
			# then update the list of tracked points
			cv2.circle(frame, (int(x), int(y)), int(radius),
				(0, 255, 255), 2)
			cv2.circle(frame, center, 5, (0, 0, 255), -1)
	# update the points queue
	pts.appendleft(center)

    # loop over the set of tracked points
	for i in range(1, len(pts)):
		# if either of the tracked points are None, ignore
		# them
		if pts[i - 1] is None or pts[i] is None:
			continue
		# otherwise, compute the thickness of the line and
		# draw the connecting lines
		thickness = int(np.sqrt(args["buffer"] / float(i + 1)) * 2.5)
		cv2.line(frame, pts[i - 1], pts[i], (0, 0, 255), thickness)
	# show the frame to our screen
	cv2.imshow("Frame", frame)
	cv2.imshow("Mask", result)
	cv2.imshow("HSV", hsv)
	key = cv2.waitKey(1) & 0xFF
	# if the 'q' key is pressed, stop the loop
	if key == ord("q"):
		break
# if we are not using a video file, stop the camera video stream
if not args.get("video", False):
	vs.stop()
# otherwise, release the camera
else:
	vs.release()
# close all windows
cv2.destroyAllWindows()