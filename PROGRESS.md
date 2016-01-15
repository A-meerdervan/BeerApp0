##Progress log
**Author** Alex van der Meer  
**Start date:** 5-1-2016

**Vr 15-1-13**  
Doelen van de dag:  
Data base implementeren voor alle aanbiedingen, en daarmee de app een stuk sneller maken
beginnen met het maken van de FilterAndSorter class en zijn functionaliteit. 

Gedaan:  
Data base geimplementeerd voor alle aanbiedingen. De app is nu een stuk sneller. Op dit moment
laad ie bij het eerst opstarten alle discount informatie van het internet, en daarna haalt ie het uit zijn
database. Ik heb de class FilterAndSorter aangemaakt. Deze moet alle filter en sorteer taken op zich nemen.
Sorteren op prijs is toegevoegd. Nu laat de app overal de kortingen zien gesorteerd op prijs van laag naar hoog.  
Verder is toegevoegd dat men nu een maximale prijs kan invoeren voor de aanbiedingen waar hij een notificatie voor wil
ontvangen. 

**Do 14-1-13**  
Doelen van de dag:  
Zorgen dat de gebruiker zijn postcode in kan voeren en dan specifieke actuele aanbiedingen krijgt.
De database opzetten in alle activities

Gedaan:
Zorgen dat de gebruiker zijn postcode in kan voeren en dat die dan specifieke aanbiedingen krijgt.
De notifcatie instellingen fragment zijn format een stuk opgeschoont. 


**Wo 13-1-2016**  
Doelen van de dag:  
In de app via een fragment je locatie in kunnen voeren en de straal, en dan alle aanbiedingen zien.
Database maken en overal implementeren

Gedaan:  
Communicatie tussen de notifcatie settings fragment en de activity opgezet. (Dit koste veel moeite)
Nu is het mogelijk om in de app de lengte en breedte graden in te voeren en de straal, dan worden alleen de matchende
aanbiedingen weergegeven.  Helaas is het niet gelukt om aan de database te beginnen.  

Functionaliteit is toegevoegd om een postcode in Nederland om te zetten in een lengte en breedte graad
Dit is gedaan met de google geoCoding API
Instructies verkregen van:
https://developers.google.com/maps/documentation/geocoding/intro

**Di 12-1-2016**  
Doelen van de dag:
Actuele supermarkt info model class schrijven en linken aan de fragment invul informatie in de app. 

Gedaan:  
Erachter gekomen dat er een bug in de google API zit:
Wanneer je meerdere zoektermen invult gescheiden met een | teken (or),  
dan is het zo dat er niet meer op afstand word geselecteerd zoals wel gespecificeerd door rankby:distance
Dat is erg jammer. En dat je maximaal 20 resultaten gereturned krijgt.
Hierdoor is het niet mogelijk om alle ondersteunde supermarkten gescheiden met | tekens te zoeken, 
Maar moet ik het doen met de rankby:importance doen en genoegen nemen met het ontbreken van een hoop supermarkten.
Een mogelijke oplossing zou zijn om per supermarkt zoekwoord een API request te sturen. 
Echter heb je 1000 requests gratis op je API sleutel en word dat snel overtreden. Daarnaast zou dat veel netwerk kosten.

Besloten een model class Supermarket toe te voegen voor supermarkt objecten met een keten naam en individuele naam en een adres veld op het moment

Gevonden dat je met latitude en longitude coordinaten de afstand tussen twee punten kunt berekenen met een formule  
http://www.movable-type.co.uk/scripts/latlong.html  
Dit kan later gebruikt worden om de afstand tot een supermarkt weer te geven en daarmee dan te kunnen sorteren op afstand. 

Besloten een class te maken: supportedSupermarketMap, een hashmap met alle ondersteunde supermarkten erin.
De code hiervoor is geinspireerd door:  
http://www.coderanch.com/t/386333/java/java/HashMap-Construction-initial-values  

Succesvol de gevonden supermarkten gefilterd op alleen de ondersteunde supermarkten. 




*Ma 11-1-2016**  
Doelen van de dag:  
Supermarkt info van google places API succesvol verkrijgen en tonen in een app

Een test android project gemaakt om supermarkt info van google te krijgen.
Deze library genomen om JSON om te zetten naar bruikbare variablen.
http://mvnrepository.com/artifact/org.json/json

**Vr 7-1-2016**  
Doelen van de dag:  
Design document maken
Classen structuur verzinnen en documenteren
Het android prototype afmaken

Gedaan:  
Deze API gevonden van google maps om te zoeken naar supermarkten.:
https://developers.google.com/maps/documentation/javascript/places#TextSearchRequests
Een indeling in model classes gemaakt voor het project
Design document gemaakt

**Do 7-1-2016**  
Doelen van de dag:  
Design document maken
Classen structuur verzinnen en documenteren
Het android prototype maken. 

Gedaan:  
Het prototype aangevuld tot het punt waar er alleen nog 1 fragment scherm mist
Besloten om een fade in, fragment te gebruiken voor het filter en de notificatie instellingen, in plaats van een swipe naar links scherm. Dit omdat het minder tijd kost om te implementeren. 

**Wo 6-1-2016**  
Doelen van de dag:
Succesvol de data over kratjes aanbiedingen parsen van http://www.bierindeaanbieding.nl/bieraanbiedingen.html, Discount objecten maken en vullen met de data van de site

Gedaan:  
Een DiscountObject class is aangemaakt om alle data per aanbieding in kwijt te kunnen. 
Een HtmlParser class is gemaakt om de benodigde data van het web te kunnen rippen. 
Een custom listview om de alle aanbiedingen in weer te geven is gemaakt in de AlleAanbiedingenActivity in Android studio.

**Di 5-1-2016**  
Doelen van de dag:  
Erachter komen hoe ik in android/java een website kan rippen van data.

Gedaan:  
De volgende java library word veelal aangeraden: JSOUP
Van deze site is de JSOUP library gehaald voor het parsen van HTML in java.
http://jsoup.org/
Deze library in android geintegreerd en eerste dingen ge parsed. 



**Ma 4-1-2016**  
Project Proposal geschreven
Besloten dat ik een notifcatie aanbiedingen, en een “alle aanbiedingen” sectie ga aanmaken. 




