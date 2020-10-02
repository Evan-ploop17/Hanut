package com.example.hanut

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.item_product.view.*

class ProductAdapter( private val mContext : Context, private val listProduct: List<Product>)
    : ArrayAdapter<Product>(mContext,0, listProduct) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //return super.getView(position, convertView, parent)
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_product, parent, false)
        val product = listProduct[position]

        layout.nameText.text = product.name
        layout.descriptionTitle.text = product.description
        layout.priceText.text = "$${product.price}"
        layout.imageView.setImageResource(product.image)
        return layout

    }
}