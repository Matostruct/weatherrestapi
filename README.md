# WeatherRESTAPI
Einfacher REST-Service, der mithilfe der Weatherstack-API die aktuelle Temperatur einer Stadt liefert. Ziel ist eine reproduzierbare Delivery-/Deployment-Lösung als Docker-Image inklusive kleiner Tests.

## Zielsetzung
- **REST-Endpunkt:** `GET /weather?city=<Stadt>` liefert Temperatur (Standard: Celsius) als JSON.
- **Quelle:** WeatherAPI (Current Weather API, kein Billing-Zwang für Free-Tier).
- **Lieferform:** Docker-Image, direkt nutzbar für Tester:innen und App-Entwickler:innen.
- **CD-Fokus:** reproduzierbare Builds, lauffähige Pipeline, klare Dokumentation.

## Voraussetzungen
- WeatherAPI API Key (https://www.weatherapi.com)
- Docker (zum Bauen/Starten des Images)
- curl oder ein anderer HTTP-Client zum Testen

## Konfiguration
Erwartete Umgebungsvariablen:
- `WEATHERSTACK_API_KEY` – persönlicher API Key
- `PORT` – optionaler HTTP-Port der Anwendung (Default z. B. 3000)

## Lokale Ausführung (Beispiel)
1. `.env` anlegen und API-Key setzen:
	 ```bash
	echo "WEATHERAPI_KEY=<dein-key>" > .env
	 ```
2. Anwendung starten:
	 ```bash
	 mvn spring-boot:run
	 ```

## API-Nutzung
- **Endpoint:** `GET /weather?city=Vienna`
- Beispiel im Browser http://localhost:3000/weather?city=Vienna
- **Antwort (Beispiel):**
```json
{
  "location": {
  "name": "Vienna",
  "region": "Wien",
  "country": "Austria",
  "lat": 48.2,
  "lon": 16.3667,
  "tz_id": "Europe/Vienna",
  "localtime_epoch": 1768645858,
  "localtime": "2026-01-17 11:30"
  },
  "current": {
  "last_updated_epoch": 1768645800,
  "last_updated": "2026-01-17 11:30",
  "temp_c": 1.1,
  "condition": {
	"text": "Partly cloudy", 
    "icon": "//cdn.weatherapi.com/weather/64x64/day/116.png",
    "code": 1003
  },
  "wind_kph": 18.4,
  "humidity": 86,
  "feelslike_c": -3.6,
  "uv": 0.2
  }
}
```
- **Fehlerfälle:**
	- 400 bei fehlendem Parameter `city`
    - 403 – API Key fehlt oder ungültig
	- 404 falls Stadt nicht gefunden
	- 502 falls OpenWeather nicht erreichbar

Hinweis: Spring liefert ohne eigenes Error-Handling die „Whitelabel Error Page“. Sobald Exception-Handler geschrieben sind, werden JSON-Antworten für Fehler standardisiert.

- **Fehlerantwort (Beispiel):**
```json
{
  "error": "Not Found",
  "message": "City not found",
  "status": 404
}
```
oder:
```json
{
  "error": "Bad Request",
  "message": "Required parameter 'city' is missing",
  "status": 400
}
```

## Kleiner Testfall
Prüft den glücklichen Pfad (Beispiel mit curl und http-Code):
```bash
curl -i "http://localhost:3000/weather?city=Vienna"
```
Windows:
```bash
curl.exe -i "http://localhost:3000/weather?city=Vienna"
```
Erwartet: HTTP 200 und JSON mit Feld `location`.

## Continuous Delivery (Vorschlag)
- Linter/Testlauf (z. B. `npm test`) auf jedem Push.
- Docker-Build & Push in ein Container-Registry (z. B. GitHub Container Registry) per CI-Workflow.
- Optional: automatischer Deploy in Staging-Umgebung (z. B. Render/Heroku/Kubernetes Namespace).

## Team
- Patrick Riedler
- Matio Tawdrous