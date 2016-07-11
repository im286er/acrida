package net.gility.acrida.android;

interface INoticeService
{ 
   void scheduleNotice();
   void requestNotice();
   void clearNotice(int uid,int type);
}