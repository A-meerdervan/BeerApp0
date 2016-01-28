## Bier aanbieding notificaties

**Author**: Alex van der Meer  
**Start date**: 4-1-2016  
**Platform**: Android

#### Een korte beschrijving van de app
Het doel van deze app is om een notificatie te sturen wanneer het favoriete bier van de gebruiker, bij een supermarkt in de buurt in de aanbieding is. 

#### Probleem beschrijving en applicatie features
Wanneer men een feestje geeft levert het een grote reductie in kosten op wanneer bier in de aanbieding wordt gekocht. Dit is op zon moment makkelijk op te zoeken op een aanbiedingen site. Echter de kosten van bier voor standaard gebruik door het jaar heen zouden ook goed gedrukt kunnen worden door het kopen van bier in de aanbieding. Door het jaar heen is het al snel te veel moeite om te zoeken naar een aanbieding. Deze app voorkomt het actief zoeken naar een aanbieding, door de gebruiker een notificatie te sturen over de nieuwste aanbieding in de buurt.  

Te veel notificaties kunnen irriteren, daarom is het mogelijk om aan te geven welke afstand je maximaal wilt reizen vanaf bijvoorbeeld je huis, om bij een supermarkt te komen. De locatie van de gebruiker word ingevoerd aan de hand van een postcode. Om verder de informatie stroom te beperken is het mogelijk voor een gebruiker om aan te geven welk of welke bier merken hem/haar interesseren. Ook is er een maximum prijs in te stellen voor wanneer de gebruiker bijvoorbeeld alleen geinteresseerd is in kratten onder de negen euro.  

Verder is het mogelijk om te kijken naar een overzicht van alle aanbiedingen die op dat moment bekend zijn. De aanbiedingen kunnen worden gesorteerd op prijs, supermarkt naam en biermerk. Alles kan gefiltered worden op maximale pijs, biersoorten en supermarkt ketens.

#### Welke aanbiedingen ondersteund worden
De app ondersteund alleen aanbiedingen in de vorm van een gehele krat van 24 en soms 12 flesjes.  
De app ondersteund alleen supermarkten, geen groothandels.  
**De volgende supermarkten worden ondersteund:**  
Albert Heijn  
Aldi  
Agrimarkt  
Attent  
Coop  
C1000  
Deen  
DekaMarkt  
Dirk van den Broek  
Edeka  
EMTÉ  
Hoogvliet  
Jan Linders  
Jumbo  
Lidl  
MCD  
Penny  
Plus  
Poiesz  
Spar  
Super de Boer  
Vomar  
**De volgende Biermerken worden ondersteund**  
Alfa  
Amstel  
Bavaria  
Brand  
Grolsch  
Grolsch Beugel  
Grolsch Radler
Gulpener  
Hertog Jan  
Jupiler  
Keizerskroon  
Palm  
Schutters  
Warsteiner  

#### Het uiterlijk van de applicatie en navigatie

De het startscherm is screenshot A. in figuur 1. De gebruiker heeft twee keuzes, of klikken op persoonlijke aanbiedingen of op de alle aanbiedingen . De app bestaat dan ook uit twee delen. Het eerste is om in te stellen welke aanbiedingen in aanmerking komen voor een notificatie en de huidige aanbiedingen die daar aan voldoen weer te geven.  
Klikken brengt men naar screenshot B. Rechtsboven kan met klikken op de voorkeuren knop, wat leid tot screenshot C. Hier kan de gebruiker zijn voorkeuren instellen.   

![Alt text](/doc/homeScreen.png)  
*Figuur 1. Screenshot A. Het thuis scherm*  

![Alt text](/doc/notify.png)  
*Figuur 1. Screenshot B. Het notificatie overzicht*  

![Alt text](/doc/voorkeuren.png)  
*Figuur 1. Screenshot C. De voorkeuren*  

![Alt text](/doc/alleAanbiedingen.png)  
*Figuur 1. Screenshot D. Het overzicht van alle aanbiedingen*  

![Alt text](/doc/filterFragment.png)  
*Figuur 1. Screenshot E. Het filter voor alle aanbiedingen.*  
  
#### Externe afhankelijkheden
De actuele aanbiedingen informatie word momenteel van: [bierindeaanbieding.nl](http://www.bierindeaanbieding.nl/) gehaald. Verder word er de google Geocoding API gebruikt om een postcode naar een lengte en breedtegraad om te zetten. Zie [Google geocoding API](https://developers.google.com/maps/documentation/geocoding/intro). De supermarkten in de buurt worden gevonden m.b.v. de google places API, zie [Google Places API](https://developers.google.com/places/). 
