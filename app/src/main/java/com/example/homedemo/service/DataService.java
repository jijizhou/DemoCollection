package com.example.homedemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

public class DataService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    public class BookManagerService extends Service {
//        private static final String TAG = "BMS";
//        private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArray - List < Book > ();
//        private Binder mBinder = new IBookManager.Stub() {
//            @Override
//            public List<Book> getBookList() throws RemoteException {
//                return mBookList;
//            }
//
//            @Override
//            public void addBook(Book book) throws RemoteException {
//                mBookList.add(book);
//            }
//        };
//
//        @Override
//        public void onCreate() {
//            super.onCreate();
//            mBookList.add(new Book(1, " Android"));
//            mBookList.add(new Book(2, " Ios"));
//        }
//
//        @Override
//        public IBinder onBind(Intent intent) {
//            return mBinder;
//        }
//    }

}
