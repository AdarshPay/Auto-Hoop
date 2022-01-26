import cv2
# import imutils
TrDict = {'csrt': cv2.TrackerCSRT_create,
         'kcf' : cv2.TrackerKCF_create}
        # #  'boosting' : cv2.TrackerBoosting_create,
        #  'mil': cv2.TrackerMIL_create,
        #  'tld': cv2.TrackerTLD_create,
        #  'medianflow': cv2.TrackerMedianFlow_create,
        #  'mosse':cv2.TrackerMOSSE_create}
tracker = TrDict['kcf']()
#tracker = cv2.TrackerCSRT_create()
#v = cv2.VideoCapture(r'D:\mot.mp4') # video
v = cv2.VideoCapture(0)
ret, frame = v.read()
# frame = imutils.resize(frame,width=600)
cv2.imshow('Frame',frame)
bb = cv2.selectROI('Frame',frame)
tracker.init(frame,bb)
True
while True:
    ret, frame = v.read()
    if not ret:
        break
    # frame = imutils.resize(frame,width=600)
    (success,box) = tracker.update(frame)
    if success:
        (x,y,w,h) = [int(a) for a in box]
        cv2.rectangle(frame,(x,y),(x+w,y+h),(100,255,0),2)
    cv2.imshow('Frame',frame)
    key = cv2.waitKey(5) & 0xFF
    if key == ord('q'):
        break
v.release()
cv2.destroyAllWindows()