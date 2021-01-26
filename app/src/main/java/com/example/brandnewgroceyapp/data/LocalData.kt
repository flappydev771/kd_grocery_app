package com.example.brandnewgroceyapp.data

import com.example.brandnewgroceyapp.localdatabase.GroceryDao
import com.example.brandnewgroceyapp.model.Grocery
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalData @Inject constructor(val groceryDao: GroceryDao) {
    suspend fun insertGroceryData(grocery: Grocery){
        groceryDao.insertData(grocery)
    }
    fun searchDatabase(name:String,category: String):Flow<List<Grocery>>{
        return groceryDao.searchDatabase(name,category)
    }
    fun searchDatabaseByOnlyName(name:String):Flow<List<Grocery>>{
        return groceryDao.searchDatabaseByNameOnly(name)
    }
}