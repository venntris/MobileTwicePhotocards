package com.example.twicephotocards

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

@Suppress("SENSELESS_COMPARISON")
class CardAdapter (context: Context, textViewResourceId: Int, items: ArrayList<Card>): ArrayAdapter<Card>(context,textViewResourceId,items) {

    private var items: ArrayList<Card> = items

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Card {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v: View
        v = if (convertView != null ) {
            convertView as View
        }else {
            val vi: LayoutInflater = LayoutInflater.from(context)
            vi.inflate(R.layout.layoutcards, null)
        }

        var card: Card = items.get(position)
        if(card != null){

            var it:TextView = v.findViewById(R.id.idcard)
            if(it != null){
                it.setText("ID: " + card.getId())
            }

            var idol:TextView = v.findViewById(R.id.idol)
            if(idol != null){
                idol.setText("Członkini: " + card.getPersonName())
            }

            var album:TextView = v.findViewById(R.id.album)
            if(album != null){
                album.setText("Album: " + card.getAlbumName())
            }

            var qty:TextView = v.findViewById(R.id.qty)
            if(qty != null){
                qty.setText("Ilość: " + card.getQty())
            }

            var image:ImageView = v.findViewById(R.id.card)
            if(image != null){
                try {
                    Picasso.get().load(card.getImageURL()).into(image)
                    /*var istream: InputStream = URL(card.getImageURL()).getContent() as InputStream
                    var bitmap: Bitmap = BitmapFactory.decodeStream(istream)
                    image.setImageBitmap(bitmap)*/
                }
                catch (e: Exception){

                }

            }
        }

        return v
    }
}