package br.com.dio.work.manager

import android.app.Application
import br.com.dio.work.manager.data.datasource.VideoDataSource

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        //assets dentro do context
        VideoDataSource.setFromFile(assets) //recebndo a lista
    }
}