package com.und.model.mongo

import com.fatboyindustrial.gsonjavatime.Converters
import com.google.gson.GsonBuilder
import java.util.*


class EP(name: String, type: String = "String")
enum class EVENTS(var id: Int, name: String, order: Int, var properties: List<EP>) {

    SEARCHED(1, "searched", 10, listOf(
            EP("Search Term")

    )),

    PRODUCTVIEWED(2, "product viewed", 20, listOf(
            EP("Amount", "Number"),
            EP("Category"),
            EP("Product")

    )),
    ADDTOCART(3, "add to cart", 30, listOf(
            EP("Amount", "Number"),
            EP("Category"),
            EP("Product", "Number"),
            EP("Quantity", "Number")
    )),
    CHARGED(4, "charged", 40, listOf(
            EP("Amount", "Number"),
            EP("Category"),
            EP("Payment Mode"),
            EP("Product", "Number"),
            EP("Quantity", "Number"),
            EP("Delivery Date", "Date")
    ))

}

val events = mutableListOf<EVENTS>(EVENTS.SEARCHED, EVENTS.PRODUCTVIEWED, EVENTS.ADDTOCART, EVENTS.CHARGED)

class Product(var id: Int, var name: String, var price: Int, var categories: MutableList<String>, var tags: MutableList<String>,
              var properties: HashMap<String, String> = hashMapOf<String, String>())

//add products
val products = listOf<Product>(
        Product(1, "CORE JAVA VOLUME 1", 100,
                mutableListOf("Book", "Programming"),
                mutableListOf("Book", "Programming"),
                hashMapOf("Author" to "Gosling")),
        Product(2, "CORE JAVA VOLUME 2", 200,
                mutableListOf("Book", "Programming"),
                mutableListOf("Book", "Programming"),
                hashMapOf("Author" to "Gosling")),
        Product(3, "T Shirt", 100,
                mutableListOf("Cloth", "Shirts"),
                mutableListOf("Casual Wear", "Man"),
                hashMapOf("Size" to "XL", "color" to "red"))
)


//add ip, coordinates
//add users, with ip,coordinates
class User(var id: Int, var name: String, var email: String, var ip: String, var geo: GeoLocation)

val users = listOf<User>(
        //anonnymous user
        User(1, "amit", "amit@userndot.com", "192.168.0.109",
                GeoLocation(coordinate = Coordinate(1.2f, 1.3f))),
        User(2, "shiv", "shiv@userndot.com", "192.168.0.100",
                GeoLocation(coordinate = Coordinate(1.9f, 1.35f))),
        User(3, "kamal", "kamal@userndot.com", "192.168.0.102",
                GeoLocation(coordinate = Coordinate(1.4f, 1.45f))),
        User(4, "laksh", "laksh@userndot.com", "192.168.0.105",
                GeoLocation(coordinate = Coordinate(2.2f, 9.3f)))
)

val paymentmodes = mutableListOf<String>("Credit Card", "Net Banking", "COD")



fun main(args: Array<String>) {
    byuserEvents()
}


fun byuserEvents () {
    //1. select a random user
    // val randomUserId = Random().nextInt(4)
    //val user = users[randomUserId]
    for(user in users) {
        doevent(user)
    }
}
fun doevent(user:User) {
    val gson = Converters.registerOffsetDateTime(GsonBuilder()).setPrettyPrinting().create()
    //0. choose a clientid
    val clientId = 1


    //for that user(try multithread for multiple users at once)
    //2. select an event randomly, keep order in mind, use next event from next ordered events
    val randomNumberOfeventsMax = Random().nextInt(30)
    val maxsize = events.size
    var currenteventId = Random().nextInt(maxsize)
    val searchedProducts = mutableListOf<Product>()
    val viwedProducts = mutableListOf<Product>()
    val addedToCarts = hashMapOf<Product,Int>()
    for (i in 1..randomNumberOfeventsMax) {
        Thread.sleep(10000)
        var event = events[currenteventId]
        var realevent: Event? = null
        if(event == EVENTS.CHARGED && addedToCarts.isEmpty()) {
            currenteventId = currenteventId -1
            event = EVENTS.ADDTOCART
        }
        when (event) {
            EVENTS.SEARCHED -> {
                realevent = Event(EVENTS.SEARCHED.id.toString(), EVENTS.SEARCHED.name, clientId)
                val product = products[Random().nextInt(products.size)]
                searchedProducts.add(product)
                realevent.attributes = hashMapOf(
                        "Searched Term" to product.name
                )

            }
            EVENTS.PRODUCTVIEWED -> {
                realevent = Event(EVENTS.PRODUCTVIEWED.id.toString(), EVENTS.PRODUCTVIEWED.name, clientId)
                val product = products[Random().nextInt(products.size)]
                viwedProducts.add(product)
                realevent.attributes = hashMapOf(
                        "product" to product.name,
                        "amount" to product.price,
                        "category" to product.categories

                )
            }
            EVENTS.ADDTOCART -> {
                realevent = Event(EVENTS.ADDTOCART.id.toString(), EVENTS.ADDTOCART.name, clientId)
                var product = products[Random().nextInt(products.size)]
                //fixme get quantity as well if charged
                if(!viwedProducts.isEmpty()) {
                    product = viwedProducts[Random().nextInt(viwedProducts.size)]
                }
                val quantity = Random().nextInt(4)
                addedToCarts.put(product,quantity)
                realevent.attributes = hashMapOf(
                        "product" to product.name,
                        "amount" to product.price,
                        "category" to product.categories,
                        "quantity" to quantity

                )
            }
            EVENTS.CHARGED -> {
                realevent = Event(EVENTS.CHARGED.id.toString(), EVENTS.CHARGED.name, clientId)
                var product = products[Random().nextInt(products.size)]
                //fixme get quantity as well if charged
                var totalPrice = 0
                if(!addedToCarts.isEmpty()) {
                    //product = addedToCarts[Random().nextInt(viwedProducts.size)]
                    val lineItems = mutableListOf<LineItem>()
                    //addedToCarts.add(product)
                    for ((i,q) in addedToCarts) {
                        val lineItem = LineItem()
                        lineItem.price = i.price*q
                        totalPrice += lineItem.price
                        lineItem.currency = "USD"
                        lineItem.product = i.name
                        lineItem.categories = i.categories
                        lineItem.tags = i.tags
                        lineItem.quantity = q
                        lineItems.add(lineItem)
                    }
                    realevent.lineItem = lineItems
                }
                realevent.attributes = hashMapOf(
                        "amount" to totalPrice,
                        "deliverydate" to product.categories,
                        "payment mode" to paymentmodes[Random().nextInt(paymentmodes.size)]

                )
/*
            EP("Amount", "Number"),
            EP("Category"),
            EP("Payment Mode"),
            EP("Product", "Number"),
            EP("Quantity", "Number"),
            EP("Delivery Date", "Date")
 */
            }

        }
        realevent.geoLocation = GeoDetails()
        realevent.geoLocation.ip = user.ip
        realevent.geoLocation.geolocation = user.geo

       println(gson.toJson(realevent))
        if (currenteventId == events.size-1 ) {
            break
        }
        var x = Random().nextInt(maxsize-currenteventId) + currenteventId
        while (x < currenteventId) {
            x = Random().nextInt(maxsize-currenteventId) + currenteventId
        }
        currenteventId = x
    }

}