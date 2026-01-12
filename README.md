# WeatherRESTAPI
Einfacher REST-Service, der mithilfe der Weatherstack-API die aktuelle Temperatur einer Stadt liefert. Ziel ist eine reproduzierbare Delivery-/Deployment-Lösung als Docker-Image inklusive kleiner Tests.

## Zielsetzung
- **REST-Endpunkt:** `GET /weather?city=<Stadt>` liefert Temperatur (Standard: Celsius) als JSON.
- **Quelle:** Weatherstack (Current Weather API, kein Billing-Zwang für Free-Tier).
- **Lieferform:** Docker-Image, direkt nutzbar für Tester:innen und App-Entwickler:innen.
- **CD-Fokus:** reproduzierbare Builds, lauffähige Pipeline, klare Dokumentation.

## Voraussetzungen
- Weatherstack API Key (https://weatherstack.com/)
- Docker (zum Bauen/Starten des Images)
- curl oder ein anderer HTTP-Client zum Testen

## Konfiguration
Erwartete Umgebungsvariablen:
- `WEATHERSTACK_API_KEY` – persönlicher API Key (`access_key`)
- `WEATHERSTACK_BASE_URL` – optional, Default `http://api.weatherstack.com/current`
- `PORT` – optionaler HTTP-Port der Anwendung (Default z. B. 3000)

## Lokale Ausführung (Beispiel)
1. `.env` anlegen und API-Key setzen:
	 ```bash
	echo "WEATHERSTACK_API_KEY=<dein-key>" > .env
	 ```
2. Anwendung starten (je nach Implementierung, z. B. Node/Express):
	 ```bash
	 npm install
	 npm run start
	 ```
	 Passe den Startbefehl an dein Stack an (z. B. `go run`, `python app.py`).

## API-Nutzung
- **Endpoint:** `GET /weather?city=Vienna`
- **Antwort (Beispiel):**
	```json
	{
		"city": "Vienna",
		"temperature_celsius": 12.3,
		"source": "weatherstack",
		"timestamp": "2026-01-12T14:05:00Z"
	}
	```
- **Fehlerfälle:**
	- 400 bei fehlendem Parameter `city`
	- 404 falls Stadt nicht gefunden
	- 502 falls OpenWeather nicht erreichbar

## Kleiner Testfall
Prüft den glücklichen Pfad (Beispiel mit curl und http-Code):
```bash
curl -i "http://localhost:3000/weather?city=Vienna"
```
Erwartet: HTTP 200 und JSON mit Feld `temperature_celsius`.

## Continuous Delivery (Vorschlag)
- Linter/Testlauf (z. B. `npm test`) auf jedem Push.
- Docker-Build & Push in ein Container-Registry (z. B. GitHub Container Registry) per CI-Workflow.
- Optional: automatischer Deploy in Staging-Umgebung (z. B. Render/Heroku/Kubernetes Namespace).

## Team
- Patrick Riedler
- Matio Tawdrous