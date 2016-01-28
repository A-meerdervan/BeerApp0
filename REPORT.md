## Bier aanbieding notificaties

**Auteur**: Alex van der Meer  
**Start datum**: 4-1-2016  
**Inlever datum**: 28-1-2016  
**Applicatie naam**: Bier aanbieding notificaties    
**Platform**: Android

#### Een korte beschrijving van de applicatie
Het doel van deze applicatie is om een notificatie te sturen wanneer het favoriete bier van de gebruiker, bij een supermarkt in de buurt in de aanbieding is. 

#### Probleem beschrijving en applicatie features
Wanneer men een feestje geeft levert het een grote reductie in kosten op wanneer bier in de aanbieding wordt gekocht. Dit is op zon moment makkelijk op te zoeken op een aanbiedingen site. Echter de kosten van bier voor standaard gebruik door het jaar heen zouden ook goed gedrukt kunnen worden door het kopen van bier in de aanbieding. Door het jaar heen is het al snel te veel moeite om te zoeken naar een aanbieding. Deze applicatie voorkomt het actief zoeken naar een aanbieding, door de gebruiker een notificatie te sturen over de nieuwste aanbieding in de buurt.  

Te veel notificaties kunnen irriteren, daarom is het mogelijk om aan te geven welke afstand je maximaal wilt reizen vanaf bijvoorbeeld je huis, om bij een supermarkt te komen. De locatie van de gebruiker word ingevoerd aan de hand van een postcode. Om verder de informatie stroom te beperken is het mogelijk voor een gebruiker om aan te geven welk of welke bier merken hem/haar interesseren. Ook is er een maximum prijs in te stellen voor wanneer de gebruiker bijvoorbeeld alleen geïnteresseerd is in kratten onder de negen euro.  

Verder is het mogelijk om te kijken naar een overzicht van alle aanbiedingen die op dat moment bekend zijn. De aanbiedingen kunnen worden gesorteerd op prijs, supermarkt naam en biermerk. Alles kan gefilterd worden op maximale prijs, biersoorten en supermarkt ketens.

#### De classen structuur van de code

Eerst zal ik alle classen bespreken en daarna een schets laten zien waarin er verbanden tussen schermen en model classen worden aangegeven.  
Hierna volgt een beschrijving van de te gebruiken classes. Het doel word per class uitgelegd en de relatie tot elkaar. Per class worden de belangrijkste methoden gemeld en indien relevant, variablen.  

Eerst vermeld ik de 2 model classen.

**Class DiscountObject**  
Beschrijving:  
Dit is een model class met alleen variablen. Het heeft prijs, supermarkt en merk informatie over een aanbieding. 

**Class SuperMarket**  
Dit is een model class van een supermarkt. Het heeft variablen over de afstand tot de gebruiker en de locatie van de supermarkt. 

**Class DatabaseHandler**  
beschrijving:  
De database bevat alle aanbiedingen in DiscountObjects type’s en alle supermarkten in de buurt in SuperMarket type's. Deze class kan data van en naar de database schrijven. Er zijn methoden om aanbiedingen toe te voegen, alle aanbiedingen op te vragen, alle aanbiedingen te verwijderen voor wanneer de app zijn gegevens ververst. 
Dezelfde type methoden zijn er voor de supermarkten.
Variabelen:  
Database colum names  
Methods:    
storeAllDiscounts, storeAllSuperMarkets  
getAllDiscounts, getAllSuperMarkets  
deleteAllDiscounts, deleteAllSuperMarkets   

**Class HtmlParser**  
Beschrijving:  
Deze class is verantwoordelijk voor het verkrijgen van de actuele aanbiedingen data, door de site http://www.bierindeaanbieding.nl/bieraanbiedingen.html te parsen. Dit gebeurd m.b.v. de [JSOUP library](http://jsoup.org/). Er is functionaliteit om aanbiedingen in de vorm van kratten te parsen en op te slaan als een lijst van DiscountObject's. De class heeft een dataBaseHandler variable om de gevonden informatie direct in de dataBase op te slaan. Wanneer er een fout optreed doordat de site veranderd is en er daardoor een html-parsen gerelateerde error optreed dan er een methode om een notificatie aan de gebruiker te sturen met het verzoek een bug repport naar de developer te sturen.  
Variablen:  
dataBaseHandler  
Methoden:  
getDiscountsArray  
sendNotificationBugReport

**Class NightUpdate**  
Beschrijving:  
Het doel van deze class is om elke nacht de aanbiedingen info te updaten en indien er een nieuwe aanbieding is die voldoet aan de eisen van de gebruiker; een notificatie sturen. Dit gebeurd op de achtergrond, dus ook als de app niet geactiveerd is. De classe extends de android BroadcoastReceiver class zodat er geluisterd kan worden naar alarm berichten van het android systeem.  
De class bevat een inner class; CustomAsyncTask, deze classe voert alles uit op een andere thread.    
Verder heeft deze class een dataBaseHandler object om de nieuwe informatie naar de database te schrijven. De class heeft een HtmlParser object om de actuele aanbiedingen te kunnen downloaden.  
Nadat de informatie beschikbaar is kan gekeken worden of er aanbiedingen aan de notificatie criteria voldoen, dit doet getDiscountsToNotifyAndUpdateDB. Er is een functie om een notificatie te versturen en een functie die een een alarm kan zetten om bij het mislukken van een update, dit over 20 minuten opnieuwe te proberen. 
Variabelen:  
dataBaseHandler  
htmlParser  
Methoden:    
getDiscountsToNotifyAndUpdateDB  
sendNotification  
setRetryAlarm  

**Class ResetAlarmOnBoot**  
Beschrijving:  
Deze class extend ook BroadcoastReceiver. Deze classe luisterd naar een bericht van het android systeem dat de telefoon zojuist is opgestart. Het is namelijk zo dat alarms allemaal worden verwijderd als de telefoon word aangezet. Deze methode zet dan het alarm weer aan. 
Methoden:  
setUpdateAndNotifyAlarm

**Class AdresToLocation**  
Deze class heeft als doel om een postcode te veranderen in een locatie in een lengte en breedtegraad. Dit gebeurd m.b.v. de google Geocoding API. The main function is getLocationFromAddress.  
Methods:  
getLocationFromAddress

**Class SuperMarketFinder**  
Beschrijving:  
Deze class heeft als doel om te vinden welke supermarkten er in een door de gebruiker ingevoerde straal te vinden zijn. De class heeft een dataBaseHandler object nodig om resultaten op te slaan en daarnaast een AdresToLocation object om de locatie te verkrijgen. Daarna word met deze locatie de Google Places API geraadpleegd middels een HTTPS request. Dit gebeurd in de getResults method.  
De straal is maximaal 50 km en het maximum aantal resultaten is 20. Daardoor is het helaas zo dat er geen zekerheid is dat alle supermarkten gevonden worden. Hoe groter de straal hoe meer kans op fouten.
Variabelen:  
adresToLocation  
datBaseHandler  
Methoden:  
getResults

** Class SupportedSuperMarketsMap**  
Deze class bevat informatie van alle mogelijke manieren om een supermarkt te spellen en zit dit om naar een "albertheijn" bijvoorbeeld zodat dit gebruikt kan worden om afbeelding bronnen op te halen. 

** Class SupportedBrandsMap**  
Deze class heeft een vergelijkbare functie met de vorige besproken class.

**Class FilterAndSorter**  
Beschrijving:
De class heeft een DataBaseHandler object om de vereiste aanbieding informatie op te halen. Er zijn methoden om te filteren op alle door de gebruiker ingevulde criteria. Er zijn twee methoden public. De eerste is getFilteredResults. Deze methode heeft als argumenten alle filter criteria en returned dan een array van matchende DiscountObject’s.  De laatste methode returned een gesorteerde array van DiscountObject’s deze word gebruikt om bijvoorbeeld de laagste prijs bovenaan weer te geven.   
Variabelen:  
dataBaseHandler  
Methoden:  
getFilteredResults  
getSortedResults  

**Class MyListAdapter**  
Beschrijving:  
Deze class maakt een custom layout mogelijk om aanbiedingen weer te geven. Hij word gebruikt in 3 activities.  

Nu over de activity classes. 

**Activity Class HomeScreenActivity**  
Beschrijving:  
Dit is het eerste scherm van de app, het heeft twee knoppen. De eerste gaat naar de notificatie beheer activity en de ander naar de activity die alle beschikbare aanbiedingen laat zien. De class zet de eerste keer als de app opgestart word het update alarm aan (waar NightUpdate naar luisterd). Verder word alleen de eerste keer als de app opstart een update gedaan rechtstreeks via HtmlParser. De variablen en methoden zijn triviaal.

**Activity Class AlleAanbiedingenActivity**  
Beschrijving:  
In dit scherm worden alle aanbiedingen weergegeven in een listview. Daarnaast is er een fragment waarin de resultaten gesorteerd en gefiltered kunnen worden. De dataBaseHandler haalt de aanbieding informatie op en het FilterAndSorter object kan de resultaten filteren. De methode populateListView vult de listview met een custom adapter en de methode filterAndUpdateListview gebruikt het filterAndSorter object om te filteren en update daarna de listview.   
Variabelen:  
dataBaseHandler  
filterAndSorter  
Methoden:  
populateListview  
filterAndUpdateListview  

**Class FilterFragment**  
Beschrijving:  
In dit fragment kan de gebruiker instellen doormiddel van checkboxes welke bieren en supermarkten hij aanbiedingen van wil zien. Daarnaast is het mogelijk om de aanbiedingen te sorteren. 

**Activity Class NotificatieRegelActivity**  
Beschrijving:
Hier kan de gebruiker zien welke aanbiedingen op dit moment aan zijn criteria voldoen. Daarnaast is er op dezelfde manier als in de AlleAanbiedingenActivity een fragment beschikbaar. Maar in dit fragment kan men de criteria voor notificatie aanpassen.  Een dataBaseHandler object is hier nodig om de huidige aanbiedingen op te halen die aan de criteria voldoen op te halen. De populateListView methode vult de listview met aanbiedingen. setNotificationCriteria slaat de aangepaste criteria op en slaat ze op in een database tabel. Op het moment dat de gebruiker nieuwe criteria opslaat word er met een SuperMarketFinder gezocht naar de juiste supermarkten. 
Variabelen:  
dataBaseHandler
SuperMarketFinder  
Methoden:  
populateListview  
setNotificationCriteria

**Class ClosestSuperMarketActivity**  
Dit scherm laat de details van de dichtsbijzijnde supermarkt van een keten zien. De afstand, het adres en een Google maps kaartje met de locatie van het filiaal. Dit is mogelijk dankzij de Google Maps API. Het is zelfs mogelijk (Hier heb ik niets voor hoeven doen het is ingebouwd in de maps API) om een route te plannen naar de het filiaal. Ook worden de aanbiedingen weergegeven die op dat moment relevant zijn bij die winkel.

#### Veranderingen ten aanzien van het design document of belangrijke keuzes.
Ik zal een aantal veranderingen aanhalen en daar mijn keuzes voor uitleggen. 

**Welke aanbiedingen ondersteund worden**  
De app ondersteund alleen aanbiedingen in de vorm van een gehele krat van 24 en soms 12 flesjes. Voorheen had ik het over het ondersteunen van flesjes blikjes en kratten. Ik heb gekozen voor kratten omdat ik denk dat hier de meeste winst voor gebruikers in zit omdat mensen makkelijker in bulk kunnen inkopen. Verder ook tijdsoverwegingen. Wellicht ga ik later nog meer ondersteunen.   
 
De app ondersteund alleen supermarkten, geen groothandels. Hiervoor is gekozen omdat niet iedereen aankopen kan doen bij een groothandel. Welicht voeg ik later nog de keuze toe voor mensen wanneer ze da app starten, of ze daar aanbiedingen over zouden willen zien. De supermarkten die ondersteund worden worden ondersteund simpelweg omdat de site bierindeaanbieding.nl die sites ook ondersteund. 

**Meer in de database**  
Voorheen was de database alleen bedoeld voor aanbiedingen maar nu is een tabel voor supermarkten toegevoegd. De supermarkt informatie moet later opgehaald kunnen worden om op een kaartje weer te geven waar de dichtstbijzijnde supermarkt is. 

**Robuust voor update falen**  
Er is functionaliteit bijgekomen om als een update niet lukt dit daarna om de 20 minuten opnieuw te proberen om te zorgen dat de app altijd de laatste aanbiedingen heeft. Een update bleek maar 0.01Mb te zijn ongeveer, dus dit is 0.3MB per maand. Verder geeft de app nu een notificatie aan de gebruiker op het moment dat de app niet update door een error omdat de site die geparsed word veranderd is. De gebruiker krijgt dat een bericht om de bug te melden aan mij, zodat ik er wat aan kan doen. 

**Een extra scherm**  
Er is een extra scherm bijgekomen voor de gebruiker om te zien waar de dichtstbijzijnde supermarkt zit. Dit gebeurd via google maps. Hiervoor is gekozen na testen op gebruikers die vaak verbaast waren dat er bijvoorbeeld een vomar bij hen om de hoek zat. 

**De fragments**  
De fragments zijn ontworpen met het idee dat ze zouden kunnen swipen. Er was echter geen tijd om dit te implementeren.

 
#### Externe afhankelijkheden en licensies
De actuele aanbiedingen informatie word momenteel van: [bierindeaanbieding.nl](http://www.bierindeaanbieding.nl/) gehaald. Hier is geen toestemming voor gevraagd en ik moet nog uitzoeken of dit nodig is. Voor de kratten plaatjes is ook die site gebruikt. Voor een deel van de supermarkt logos is die site ook gebruikt. De rest is verkregen door met google te zoeken op logo's met een open licensie. Ik heb ook geen kennis van de problemen rond logo licencies en moet dit nog uitzoeken.

Verder word er de google Geocoding API gebruikt om een postcode naar een lengte en breedtegraad om te zetten. Dit is gratis tot 1000 keer per dag. Zie [Google geocoding API]  
(https://developers.google.com/maps/documentation/geocoding/intro).  
De supermarkten in de buurt worden gevonden m.b.v. de google places API, zie [Google Places API](https://developers.google.com/places/). Dit is ook gratis tot 1000 keer per dag. 
Verder word de [Google maps API](https://www.google.com/intx/en_uk/work/mapsearth/products/mapsapi.html?utm_source=HouseAds&utm_medium=cpc&utm_campaign=2015-Geo-EMEA-LCS-GEO-MAB-DeveloperTargetedHouseAds&utm_content=Developers&gclid=CPTXwfSlzcoCFUORGwods7UJ3g) gebruikt. Dit is gratis wanneer het google logo vermeld word. 

####Conclusie
De applicatie doet alles wat in het design document staat. Ik ben tevreden en heb mijn doelen bereikt. Het is een bruikbare applicatie geworden die ik zelf met plezier kan gebruiken en mogelijk anderen. Ik ga de applicatie verder ontwikkelen. 
