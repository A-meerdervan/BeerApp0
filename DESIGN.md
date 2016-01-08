## Design Document: Bier aanbieding notificatie applicatie

**Author**: Alex van der Meer  
**Date**: 8-1-2016  
**Platform**: Android

Eerst zal ik alle classen bespreken en daarna een schets laten zien waarin er verbanden tussen schermen en model classen worden aangegeven.  
Hierna volgt een beschrijving van de te gebruiken classes. Het doel word per class uitgelegd en de relatie tot elkaar. Per class zijn de variabelen en methoden die geimplementeerd zullen worden, weergegeven.  

**Class DiscountObject**  
Beschrijving:
Een aanbieding heeft vaste eigenschappen, deze worden opgeslagen in dit data type. De class heeft geen methoden. De volgende dingen worden opgeslagen, het bier merk, het formaat zoals bijvoorbeeld 24 flesjes van 0.3l, de prijs, de prijs per liter, de supermarkt, tot wanneer de aanbieding geld, het type, of het over kratten, flesjes, of blikjes gaat, ook is er een variabele om de afstand tot de gebruiker in op te slaan en als laatste het id nummer voor gebruik in de database.  
Variabelen:  
brand  
format  
price  
pricePerLiter  
superMarkt	  
discountPeriod	  
type  
distance  
id  
Methoden:  
Geen

**Class DatabaseHandler**  
beschrijving:
De database bevat alle aanbiedingen in DiscountObjects type’s. Deze class kan data van en naar de database schrijven. Er zijn methoden om aanbiedingen toe te voegen, alle aanbiedingen op te vragen, alle aanbiedingen te verwijderen voor wanneer de app zijn gegevens ververst en een methode om een aanbieding te updaten. De laatste kan gebruikt worden om de afstand tot de gebruiker erin te zetten.  
Variabelen:  
Database colum names  
Methods:  
addDiscount  
getAllDiscounts  
deleteAllDiscounts  
updateDiscount  

**Class HtmlParser**  
beschrijving:
Deze class is verantwoordelijk voor het verkrijgen van de actuele aanbiedingen data, door de site http://www.bierindeaanbieding.nl/bieraanbiedingen.html te parsen. Er is functionaliteit om aanbiedingen van het krat, fles en blik type te parsen. De class heeft een dataBaseHandler variable om de gevonden informatie direct in de dataBase op te slaan.   
Variablen:  
dataBaseHandler  
Methoden:  
getKrateDiscountArray  
getBottleDiscountArray  
getCanDisountArray  

**Class FilterAndSorter**  
Beschrijving:
De class heeft een DataBaseHandler object om de vereiste aanbieding informatie op te halen. Er zijn methoden om te filteren op alle door de gebruiker ingevulde criteria. Er zijn twee methoden public. De eerste is getFilteredResults. Deze methode heeft als argumenten alle filter criteria en returned dan een array van matchende DiscountObject’s.  De laatste methode returned een gesorteerde array van DiscountObject’s deze word gebruikt om bijvoorbeeld de laagste prijs bovenaan weer te geven.   
Variabelen:  
dataBaseHandler  
Methoden:  
getFilteredResults  
getSortedResults  

**Class SuperMarketFinder**  
Beschrijving:
Deze class zal een google maps API gebruiken om met de locatie die de gebruiker heeft ingevoerd en een ingevoerde straal (maximaal 50 km) een lijst te geven van de supermakten in de buurt en de afstand tot die supermarkten. De class word aangeroepen vanuit de NightUpdate class en deze beschikt over de locatie en straal informatie. Eerst word het adres omgezet in het juiste locatie object type met getLocationPointFromAdress en vervolgens worden de supermarkten in de buurt gegeven door getSuperMarketsInRange. Deze methode heeft een locatie object en een straal als argumenten en returned een lijst met supermarkten met bijbehorende afstand tot de gebruiker.    
Variabelen:  
geen  
Methoden:  
getLocationPointFromAdres  
getSuperMarketsInRange  

**Class NightUpdate**  
Beschrijving:
Het doel van deze class is om elke nacht de aanbiedingen info en supermarkt info te updaten ookal is de app niet geactiveerd.  De tijd waarop dit moet gebeuren is een variabele. Verder heeft deze class een dataBaseHandler object om de nieuwe informatie naar de database te schrijven. Er is een superMarketFinder object om de superMarkten in de buurt te vinden en htmlParser object om de actuele aanbiedingen te kunnen downloaden.  Er is een methode die in 1 keer alles update, updateInformation. Nadat de informatie beschikbaar is kan gekeken worden of er aanbiedingen aan de notificatie criteria voldoen, dit doet getNotificationDiscounts. Indien dit het geval is kan de methode sendNotification een notificatie sturen. 
Variabelen:  
updateTime  
dataBaseHandler  
superMarketFinder  
htmlParser  
Methoden:  
updateInformation  
getNotificationDiscounts  
sendNotification  

Nu over de activity classes. 

**Activity Class HomeScreenActivity**  
Beschrijving:
Dit is het eerste scherm van de app, het heeft twee knoppen. De eerste gaat naar de notificatie beheer activity en de ander naar de activity die alle beschikbare aanbiedingen laat zien. De variablen en methoden zijn triviaal.

**Activity Class AlleAanbiedingenActivity**  
Beschrijving:
In dit scherm worden alle aanbiedingen weergegeven in een listview. Daarnaast is er een fragment waarin de resultaten gesorteerd en gefiltered kunnen worden. De dataBaseHandler haalt de aanbieding informatie op en het FilterAndSorter object kan de resultaten filteren. De methode populateListView vult de listview met een custom adapter en de methode filterAndUpdateListview gebruikt het filterAndSorter object om te filteren en update daarna de listview. 
Variabelen:  
dataBaseHandler  
filterAndSorter  
Methoden:  
populateListview  
filterAndUpdateListview  

**Activity Class NotificatieRegelActivity**  
Beschrijving:
Hier kan de gebruiker zien welke aanbiedingen op dit moment aan zijn criteria voldoen. Daarnaast is er op dezelfde manier als in de AlleAanbiedingenActivity een fragment beschikbaar. Maar in dit fragment kan men de criteria voor notificatie aanpassen.  Een dataBaseHandler object is hier nodig om de huidige aanbiedingen op te halen die aan de criteria voldoen op te halen. De populateListView mehtode vult de listview met aanbiedingen. setNotificationCriteria slaat de aangepaste criteria op en slaat ze op in een database tabel. 
Variabelen:  
dataBaseHandler  
Methoden:  
populateListview  
setNotificationCriteria  


####Een verbindend figuur

In figuur 1 is de relatie tussen schermen en model classen aangegeven met pijlen en kleine beschrijvingen. De naam van een class is overal in een rechthoek geplaatst. linksonderin is er een rondje gebruikt om een proces aan te geven dat in de achtergrond draait. Op dezelfde manier als voorheen zijn er met pijlen de relaties tussen classen en dit process aangegeven.  

![Alt text](/doc/ClassScherm.jpg)  
*Figuur 1. De relatie tussen schermen en model classen*


####Lijst van gebruikte API’s en Frameworks en een data bron
Voor het parsen van html content over actuele aanbiedingen word de libriary JSOUP gebruikt. 
Bron: http://jsoup.org/
De site waar de benodigde data van daan word gehaald is:
http://www.bierindeaanbieding.nl/bieraanbiedingen.html

Voor het vinden van de lijst met supermarkten binnen een bepaalde straal van de gebruiker heb ik dit gevonden wat wel het juiste resultaat geeft, maar het is in javascript.  
 
https://developers.google.com/maps/documentation/javascript/places#TextSearchRequests
Ik moet nog uitzoeken hoe het in android kan, maar er moet wat te regelen zijn. 