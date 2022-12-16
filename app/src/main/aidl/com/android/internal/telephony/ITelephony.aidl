// ITelephony.aidl
package com.android.internal.telephony;
interface ITelephony{
    boolean endCall();//挂断电话
    void answerRingingCall();
}