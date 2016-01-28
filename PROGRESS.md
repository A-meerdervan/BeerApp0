##Progress log
**Author** Alex van der Meer  
**Start date:** 5-1-2016

**Vr 25-1-13**  
Doelen van de dag:  
Gedaan: 

**Do 25-1-13**  
Doelen van de dag:  
Verslag inleveren.
Gedaan: 
Erachter gekomen dat de app snachts zichzelf niet heeft geupdate. Ik ben nu bezig de bug te proberen te vinden.



**Wo 25-1-13**  
Doelen van de dag:  
De app mooier maken, en de gebruikers ervaring meer gestroomlijnd. App laten testen door mensen en feedback verkrijgen. Deze feedback vervolgens verwerken.

Gedaan: 
Verder gewerkt aan het UI design van de app, de juiste text berichten sturen in de juiste gevallen, zoals dat het filter geen resultaten heeft opgeleverd. Na aandringen van mensen het invoeren van de afstand tot een supermarkt in meters veranderd naar kilometers. 

**Di 25-1-13**  
Doelen van de dag:  
Het werkend krijgen van het kaartje met de dichtbijzijnde supermarkt.  
Een walkthrough van de app geven aan de gebruiker wanneer deze voor het eerst de app opstart.
Allerlei bugs fixen.  
Werken aan de opmaak van de app.

Gedaan:
De hele dag ben ik bezig geweest met het stylen van de app. Ik heb naar andere apps op mijn telefoon gekeken en proberen na te doen hoe zij kleuren gebruiken. Het blijkt dat zo'n beetje alle professionele apps het volgende doen; Een witte tot grijze achtergrond, een navigation bar in een bepaalde kleur. Daarnaast wat grotere text, knoppen en highlights in een andere kleur of in dezelfde als de navigation bar. Als hulp heb ik deze site gebruikt om kleuren te kiezen:  
https://color.adobe.com/nl/create/color-wheel/?base=2&rule=Complementary&selected=4&name=Mijn%20Color-thema&mode=rgb&rgbvalues=0.5659474690579943,0.7,0.6056493332131362,1,0.657184212716933,0.37074156432223626,1,0.4552066879900849,0,0,0.6728781805825123,0.796078431372549,0.17382961954088505,0.8721427511955415,1&swatchOrder=0,1,2,3,4  

**Ma 25-1-13**  
Doelen van de dag:  
Regelen dat de app uitrekent welk filiaal het dichts bij jou in de buurt is, en dat dit weergegeven word in een activity wanneer je klikt op een voor notificatie geschikt item.  
Een begin maken aan het op een kaartje afbeelden waar de supermarkt is en waar jij bent in diezelfde activity.

Gedaan:  
Julian wou een demo. Waar moest het bier voor de borrel van daan halen?. Top, ik dit laten zien, bleek dat de aanbiedingen niet actueel waren. Na wat uitzoekwerk bleek dat de makers van de site die ik parse, een regel code hadden veranderd, daardoor eindigde de nachtelijke update in een error. Dit gebeurde in mijn mobiel dus merkte ik niets van de error. Het is aangepast dus nu is de site weer een stapje robuuster.  
Het volgende toegevoegd; Van alle supermarkt resultaten in de buurt word de afstand tot de gebruiker berekend. Dan word per keten de dichtsbijzijnde gevonden en gemarkeerd in de database met een closestFlag eigenschap. Op het moment dat een gebruiker op een list item van de resultaten voor notificatie lijst klikt, dan opent er een nieuwer activity. In deze activity zijn de gegevens van de supermarkt zichtbaar, en de huidige aanbiedingen van die supermarkt die voldoen aan de voorkeuren van de gebruiker. Er moet ook een kaartje te zien zijn met de locatie van de winkel op een kaartje via google maps.  
Zojuist heb ik daar de volgende API key voor aangevraagd:  AIzaSyDD513nMZmEMyg_dPZBSiiFs3lS7K7_rGg.  
In verassend weinig tijd is het gelukt om de map view werkend te krijgen.   



**Zo 24-1-13**  
Doelen van de dag:  
Paar bugs fixen

Gedaan:  
Na een user test bleek dat het misleidend was dat de listview over alle aanbieidingen highlight als je op een item klikt,
terwijl er niets gebeurd. Ik heb deze feature uitgezet. Verder bleek dat i.p.v. maximum prijs maximum prijs per krat duidelijker zo zijn. Dit is aangepast. 

**Za 21-1-13**
Doelen van de dag:  
Het filteren in de alle aanbiedingen activity regelen.

Gedaan:  
Fixen dat op het moment dat je geen internet connectie hebt de eerste keer dat je de app opstart dat je dan niet door kunt klikken naar een volgend scherm en een textview met rode achtergrond te zien krijgt dat je eerst internet moet hebben.
Custom checkboxen programmatisch toegevoegd aan de filter fragment. Ze worden uitgelezen en een list word geproduceerd met aangefinkte boxes. Deze lists worden dan doorgestuurd naar de activity die zijn listview met aanbiedingen hierop kan filteren. Het filteren gebeurd in de FilterAndSorter class.  
In deze class is er een functie gemaakt genaamd filterAndSort. Deze functie filtered op alle mogelijke filter opties.
Voor het sorteren op alfabetische volgorde van supermarkten en aanbiedingen is deze bron van pas gekomen:  
http://stackoverflow.com/questions/19471005/sorting-an-arraylist-of-objects-alphabetically  



**Vr 21-1-13**
Doelen van de dag:  
De filter fragment werkend krijgen zodat het mogelijk word om alle aanbiedingen te filteren.

Gedaan:  


**Do 21-1-13**
Doelen van de dag:  
Proberen te regelen dat als een alarm om te updaten afgaat, maar er geen internet connectie is. Dat de app dan blijft proberen om de zoveel tijd totdat het wel lukt. 

Gedaan:  
Wanneer het niet lukt om de data binnen te halen op het moment dat snachts geplanned is. Dan word er een appart alarm ingeschakeld dat elke 20min opniew probeerd. Het is getest een "snachts alarm" van om de 1 minuut en een "retry alarm" van om de 10 seconden. Wanneer ik de vliegtuigstand aan doe en weer uit dan zag ik dat het werkt.  
Toegevoegd dat alleen wanneer de app voor het eerst word geopend, er een update alarm word gezet.  
Verder toegevoegd dat waneer de app voor het eerst word geopend en er geen internet connectie is, de app een dialog geeft. De dialog zegt de gebruiker dat er geen connectie is en dat hij kan proberen de app opniew op te starten. Wanneer de app opnieuw opstart dan word er weer geprobeerd om discount info te parsen. De dialog blijft komen todat het een keer wel gelukt is.

**Wo 20-1-13**
Doelen van de dag:  
Notificaties sturen op de juiste plek met de juiste inhoud.
Uitzoeken hoe ik processen op de achtergrond kan draaien om de app up te daten en notifcaties te sturen.

Gedaan:  
Notificaties laten sturen met de juiste inhoud. 
Op deze pagina uitgevonden hoe je een class moet indelen die moet luisteren naar de broadcoast van een alarm en de
bijbehorende taken moet uitvoeren (De class heet NightUpdate). De code was geinspireerd door deze tutorial:  
http://www.sitepoint.com/scheduling-background-tasks-android/  
Een process op de achtergrond weten aan te roepen die om een vastgestelde hoeveelheid tijd de discount info update. Dit gebeurd via een receiver alarm. Het alarm roept een class aan genaamt NightUpdate. Deze class extends BroadcoastReceiver om aangeroepen te kunnen worden door android. Het alarm word uitgeschakelt op het moment dat de telefoon uit gaat. Ik heb een andere class gemaakt genaamt ResetAlarmOnBoot, die luisterd naar een broadcoast bericht dat de telefoon is aangedaan en dat het periodieke alarm opniew inschakeld. 
Ik heb functionaliteit toegevoegd om erachter te komen welke aanbiedingen nieuw zijn en waar nog geen notificatie van is geweest. Nu stuurt de app niet meerdere malen een notificatie over dezelfde aanbieding. 

**Di 19-1-13**  
Bijzonderheden:  
Deze dag ging ik naar een Master voorlichting in Enschede.  
Daardoor heb ik weinig kunnen doen.

Doelen van de dag:  
Notificaties uitzoeken  

Gedaan:  
De app een simpele notificatie laten sturen maar nog niet met de juiste inhoud en op het juiste moment. 
Code geinspireerd door:  
http://developer.android.com/guide/topics/ui/notifiers/notifications.html#Removing  

**Ma 18-1-13**  
Doelen van de dag:  
Regelen dat de gebruiker zijn favoriete bier in kan vullen middels een drop down spinner
Notificaties sturen regelen

Gedaan:  
Dropdown van bieren gefixt, door via java textviews toetevoegen aan de layout. En erop laten filteren in het resultaat en
 op te slaan voor de volgende keer dat de app opent.


**Za 16-1-13**  
Doelen van de dag:  
Database verder in de app toepassen

Gedaan:  
Erachter gekomen dat een aantal supermarkten niet werden opgepikt door mijn code omdat ze anders gespeld waren
dan ik had verwacht. Zoals de appostrof bij de Emte, en een plus die soms helemaal in hoofdletters word geschreven.
Dit gold ook voor de spar. Inmiddels zijn de All-caps versies van alle supermarkten toegevoegd om dit probleem te
voorkomen in bij andere supermarkten.
Er is toegevoegd dat de app de notificatie instellingen van de gebruiker onthoud middels sharedprefferences. De gebruiker
zijn vorige instellingen zijn al ingevuld wanneer de gebruiker nu naar de instellingen pagina gaat. 
In het notificatie overzicht is toegevoegd dat er middels een textview een bericht aan de gebruiker getoond word,
op het moment dat de aanbiedingen database leeg is, en waneer de gebruiker op dit moment geen aanbiedingen heeft
die aan zijn voorkeuren voldoen.
De manier van invoeren van de postcode bij de instellingen is intuitiever gemaakt na een test met een gebruiker. 
Bron gevonden om naar notificaties te kijken:  
http://www.tutorialspoint.com/android/android_push_notification.html  
Tijdens het laden van data van de API's en het HTML parsen is een load spinner toegevoegd.
De code is geinspireerd door de volgende link:  
http://stackoverflow.com/questions/5442183/using-the-animated-circle-in-an-imageview-while-loading-stuff  

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




