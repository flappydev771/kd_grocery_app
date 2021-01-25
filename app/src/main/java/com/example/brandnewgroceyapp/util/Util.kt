package com.example.brandnewgroceyapp.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import com.bumptech.glide.request.RequestOptions
import com.example.brandnewgroceyapp.R
import com.example.brandnewgroceyapp.model.Category
import com.glide.slider.library.SliderLayout
import com.glide.slider.library.animations.DescriptionAnimation
import com.glide.slider.library.slidertypes.TextSliderView
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar


class Util {

    companion object {

        private fun getImageUrls(): List<Int> {
            val imageUrls: MutableList<Int> = ArrayList()

            imageUrls.add(R.drawable.breakfast)

            imageUrls.add(R.drawable.vegetables)

            imageUrls.add(R.drawable.fish)

            imageUrls.add(R.drawable.dairy)
            return imageUrls
        }

        private fun getImageTexts(): List<String> {
            val texts: MutableList<String> = ArrayList()
            texts.add("Breakfast")
            texts.add("Vegetables")
            texts.add("Fish")
            texts.add("Dairy")
            return texts
        }

        fun setSliders(view: View) {
            val sliderLayout: SliderLayout = view.findViewById(R.id.slider)

            val requestOption = RequestOptions()


            for (i in getImageUrls().indices) {

                val sliderView = TextSliderView(view.context)

                sliderView.image(getImageUrls()[i])
                    .description(getImageTexts()[i])
                    .setRequestOption(requestOption.centerCrop())
                    .setProgressBarVisible(true)
                Log.e("num: ", "" + i)


                sliderLayout.addSlider(sliderView)

            }

            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion)
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
            sliderLayout.setDuration(2000)
            sliderLayout.stopCyclingWhenTouch(false)
            sliderLayout.setCustomAnimation(DescriptionAnimation())
        }

        fun getCategoryImages(): List<String> {
            val categoryImages: MutableList<String> = ArrayList()

            categoryImages.add("https://cdn-b.medlife.com/2018/01/fruits-and-vegetables-to-consume-in-winter.png")
            categoryImages.add("https://food.fnr.sndimg.com/content/dam/images/food/fullset/2019/5/13/0/FNK_Sheetpan-Breakfast-Bake_H2_s4x3.jpg.rend.hgtvcom.826.620.suffix/1557773594845.jpeg")
            categoryImages.add("https://www.jbtc.com/-/media/images/foodtech/markets/juice-beverage.ashx")
            categoryImages.add("https://i0.wp.com/post.medicalnewstoday.com/wp-content/uploads/sites/3/2020/02/iStock-1165495226-1024x683.jpg?w=1155&h=2969")
            categoryImages.add("https://www.foodbusinessnews.net/ext/resources/2019/5/SnackVariety_Lead.jpg?t=1558527976&width=1080")
            categoryImages.add("https://www.dairyfoods.com/ext/resources/DF/2020/August/df-100/GettyImages-1194287257.jpg?1597726305")
            categoryImages.add("https://cdn-a.william-reed.com/var/wrbm_gb_food_pharma/storage/images/7/6/7/8/1188767-1-eng-GB/Bread-dips-on-health-concerns-pastries-and-cakes-offer-silver-lining_wrbm_large.jpg")
            categoryImages.add("https://images.indianexpress.com/2019/07/cooking_1200.jpg")

            return categoryImages
        }
        fun getCategoryText(): List<String> {
            val categorytext: MutableList<String> = ArrayList()

            categorytext.add("Fruits & Vegetables")
            categorytext.add("Breakfast")
            categorytext.add("Beverage")
            categorytext.add("Fish & Meat")
            categorytext.add("Snacks")
            categorytext.add("Dairy")
            categorytext.add("Bread & Bakery")
            categorytext.add("Cooked")

            return categorytext

        }
         fun hasInternetConnection(application: Application): Boolean {

            val connectivityManager = application
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val networkCapabilites =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                networkCapabilites.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilites.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                networkCapabilites.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                else -> false
            }

        }
        fun showDotProgress(progressBar: DilatingDotsProgressBar){
            progressBar.showNow()
        }
        fun hideDotProgress(progressBar: DilatingDotsProgressBar){
            progressBar.hideNow()
        }

        fun getAllCategories( name:List<String>,image:List<String>):List<Category>{
            val categories:MutableList<Category> = ArrayList()
            categories.clear()

            for(i in name.indices){
           var category:Category = Category(name[i],image[i])
                categories.add(category)
            }
            return categories
        }


    }

}