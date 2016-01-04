## Project Proposal: Bier aanbieding notificatie applicatie

**Author**: Alex van der Meer  
**Start date**: 4-1-2016

#### Applicatie doelen samenvatting
Het doel van deze app is om een gebruiker een notificatie te laten ontvangen wanneer zijn of haar favoriete bier in een lokale supermarkt in de buurt is. 

#### Probleem beschrijving en applicatie features
Wanneer men een feestje geeft levert het een grote reductie in kosten op wanneer bier in de aanbieding wordt gekocht. Dit is op zon moment makkelijk op te zoeken op een aanbiedingen site. Echter de kosten van bier voor standaard gebruik door het jaar heen zouden ook goed gedrukt kunnen worden door het kopen van bier in de aanbieding. Door het jaar heen is het al snel te veel moeite om te zoeken naar een aanbieding. Deze app voorkomt het actief zoeken naar een aanbieding, door de gebruiker een notificatie te sturen over de nieuwste aanbieding in de buurt. Te veel notificaties kunnen irriteren, daarom is het mogelijk om aan te geven welke afstand je maximaal wilt reizen vanaf bijvoorbeeld je huis, om bij een supermarkt te komen. Om verder de informatie stroom te beperken is het mogelijk voor een gebruiker om aan te geven welk of welke bier merken hem/haar interesseren. 

#### Globaal uiterlijk applicatie

De het startscherm is scherm A. in figuur 1. De gebruiker heeft twee keuzes, of klikken op de Resultaten voor notificatie sectie of op de alle aanbiedingen sectie. De app bestaat dan ook uit twee delen. Het eerste is om in te stellen welke aanbiedingen in aanmerking komen voor een notificatie en de huidige aanbiedingen die daar aan voldoen weer te geven. Het start scherm geeft een preview daarvan. Klikken brengt men naar scherm B. Scherm B. geeft de huidige resultaten en heeft aan de rechterkant een verticale balk met de het woord instellingen erin. Deze kan uitgeswiped worden om bijna het hele scherm te bevatten. Dit brengt scherm D. Hierin zijn de criteria in te stellen voor wanneer een aanbieding in aanmerking komt voor een notificatie.  

![Alt text](/doc/AtotD.jpg)  
*Figuur 1. Schermen A. tot D. van de app*
  
Wanneer "Alle aanbiedingen" geklikt wordt vanaf scherm A. brengt dit scherm C. Hier is een lijst weergegeven van alle huidige aanbiedingen. Deze kunnen door swipen worden weergegeven voor Kratten, blikjes en flesjes. Aan de rechterkant van het scherm is een vericale strook zichtbaar met de letters "Filter", deze kan worden uitgeswiped. Dat leid tot scherm E. in figuur 2. Er is daar dan de mogelijkheid om de lijst de sorteren en Filters toe te passen. 

![Alt text](/doc/SchermE.jpg)  
*Figuur 2. Scherm E. van de app*

#### Data verkrijgen
Er is actuele informatie nodig over de bier aanbiedingen van alle grote supermarkten. Er zijn verschillende opties om hier aan te komen. Een mogelijkheid is [yenom.nl](http://www.yenom.nl/) yenom.nl deze site heeft alle supermarkt aanbiedingen. Een andere mogelijkheid is iets als [bierindeaanbieding.nl](http://www.bierindeaanbieding.nl/).
  
De app moet weten welke supermarkten er in de buurt van de gebruiker zijn. Om dit te doen zal [Google maps](https://www.google.nl/maps/) worden gebruikt. 

#### Programma decompositie

De app kan opgedeeld worden in verschillende taken. Ten eerste het laden van de aanbiedingen van een aanbiedingen site. Dan kan daarna worden gefiltered welke aanbiedingen wel of niet worden weergegeven. 

Een ander onderdeel is bepalen welke supermarkten er in de buurt van de locatie van de gebruiker zijn. 

Verder is het filteren van de opgehaalde aanbiedingen een belangrijke taak. Nadat de supermarkten bekend zijn en de gebruiker zijn wensen heeft ingevuld moet er gefilterd worden. 

#### Potentiele problemen
Een probleem kan zijn dat de eigenaar van een site mij wettelijk vervolgt voor het gebruiken van zijn content. De licensie zal onderzocht moeten worden. 

Als het niet lukt om via Google maps de dichtsbijzijne supermarkten te identificeren kan de gebruiker zelf de supermarkten invoeren. 

#### Soortgelijke applicaties
Op de google play store kon ik 3 andere bier aanbiedingen applicaties vinden. Deze hadden echter allemaal geen mogelijkheid om de gebruiker een notificatie te geven. De applicaties lieten van de grotere supermarkten en groothandels in het land de aanbiedingen zien. Het was mogelijk om naar aanbiedingen van kratten, flesjes en blikjes te kijken. Bij 2 van de 3 was het mogelijk om filters in te stellen voor merken en supermarkten. 