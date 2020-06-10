package com.example.twicephotocards

class Card {
    private var id: String = ""
    private var name: String = ""
    private var personName: String = ""
    private var albumName: String = ""
    private var imageURL: String = ""
    private var qty: Int = 0

    fun getId(): String
    {
        return id;
    }

    fun setId(id: String): Card{
        this.id = id
        return this
    }

    fun getName(): String
    {
        return name;
    }

    fun setName(name: String): Card{
        this.name = name
        return this
    }

    fun getPersonName(): String
    {
        return personName;
    }

    fun setPersonName(personName: String): Card{
        this.personName = personName
        return this
    }

    fun getAlbumName(): String
    {
        return albumName;
    }

    fun setAlbumName(albumName: String): Card{
        this.albumName = albumName
        return this
    }

    fun getImageURL(): String
    {
        return imageURL;
    }

    fun setImageURL(imageURL: String): Card{
        this.imageURL = imageURL
        return this
    }

    fun getQty(): Int
    {
        return qty;
    }

    fun setQty(qty: Int): Card
    {
        this.qty = qty
        return this
    }
}