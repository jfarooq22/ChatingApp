package com.example.chatingapp.items



import com.example.chatingapp.R
import com.example.chatingapp.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatToItem(val text:String, val user: User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.textview_to_row.text=text
        val uri= user.profileImageurl
        val targetimage=viewHolder.itemView.imageview_chat_to_row
        Picasso.get().load(uri).into(targetimage)

    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

}