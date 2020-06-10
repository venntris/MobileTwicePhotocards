package com.example.twicephotocards

import android.annotation.SuppressLint
import android.app.ListActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject

class showCards : ListActivity() {

    private lateinit var m_ProgressDialog: ProgressBar
    private lateinit var m_Cards: ArrayList<Card>
    private lateinit var u_Cards: ArrayList<Card>
    private lateinit var m_Adapter: CardAdapter
    private lateinit var viewCards: Runnable
    private lateinit var cat: String
    private lateinit var qtyMap: MutableMap<String, Int>

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_cards)
        //Picasso.setSingletonInstance(Picasso.Builder(this).build())
        cat = intent.getStringExtra("selectedCategory") as String
        qtyMap = mutableMapOf()
        setTitle()
        getUserCards()
    }

    fun setTitle() {
        val personField: TextView = findViewById(R.id.personView)
        val catName = intent.getStringExtra("categoryName")
        personField.text = "$catName ($cat)"
    }

    fun showCards() {
            var listView = findViewById<ListView>(android.R.id.list)
            m_Adapter = CardAdapter(this, R.layout.layoutcards, m_Cards)
            //val adapter = ArrayAdapter<Card>(this, android.R.layout.simple_list_item_1, m_Cards)
            listView.adapter = m_Adapter
            listView.requestLayout()
    }

    fun getUserCards() {
        u_Cards = ArrayList<Card>()
        try {
            GlobalScope.launch {
                val nc = NetworkConnection()
                var ucresult: String = ""
                CoroutineScope(Dispatchers.IO).launch {
                    ucresult = nc.getResult(prefs.baseUrl + "api/users/details/")
                    val cards = JSONObject(ucresult.toString()).getJSONObject("data").getJSONObject("cards")
                        unpackUserCards(cards)
                    }
                withContext(Dispatchers.Main) {
                    getCards()
                }
            }
        }
        catch (e:Exception){

        }
    }

    fun unpackUserCards(ucards: JSONObject){
        var ukeys = ucards.keys()
        for (ukey in ukeys) {
            var card = ucards.getJSONObject(ukey)
            val userCard = makeCardFromJSON(card)
            qtyMap.put(userCard.getId(), userCard.getQty())
            //u_Cards.add(userCard)
        }
    }

    fun getCards(){
        m_Cards = ArrayList<Card>()
        try {
            GlobalScope.launch {
                val nc = NetworkConnection()
                var result: String = ""
                CoroutineScope(Dispatchers.IO).launch {
                    result = nc.getResult(prefs.baseUrl + "api/cards/c/" + cat)
                    val cards = JSONObject(result.toString()).getJSONArray("data")
                    for (i in 0 until cards.length()) {
                        val cardObject = cards.getJSONObject(i)
                        var readyCard = makeCardFromJSON(cardObject)
                        var rcId = readyCard.getId()
                        if (qtyMap.containsKey(rcId)) readyCard.setQty(qtyMap[rcId] as Int)
                        withContext(Dispatchers.Main) {
                            m_Cards.add(readyCard)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        showCards()
                    }
                }
            }
        }
        catch (e:Exception){

        }
    }

    fun makeCardFromJSON(cardObject: JSONObject): Card {
        var c1 = Card()
        c1.setId(cardObject.getString("id"))
        c1.setAlbumName(cardObject.getJSONObject("subcategory").getString("name"))
        c1.setPersonName(cardObject.getJSONObject("category").getString("name"))
        c1.setName(cardObject.getString("name"))
        c1.setQty(getQty(cardObject))
        c1.setImageURL(cardObject.getString("image"))
        return c1
    }

    fun getQty(cardObject: JSONObject): Int{
        return if (cardObject.has("qty")) cardObject.getInt("qty")
        else 0
    }

    fun JSONArray.forEachString(action: (String) -> Unit) {
        for (i in 0 until length()) {
            action(getString(i))
        }
    }

    fun JSONObject.forEachString(action: (String) -> Unit) {
        for (i in 0 until length()) {
            action(getString(i))
        }
    }
}
