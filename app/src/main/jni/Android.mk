LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := BlurJni
LOCAL_SRC_FILES =: BlurJni.cpp
include $(BUILD_SHARED_LIBRARY)