# Music Service

Service that manages calls to the Spotify API and returns every music related info needed for the application

---
## Search { api/v1/search }

Search for music tracks, albums or artists that match given query

### Endpoints

* **`GET /album`** - Returns list of albums

* **`GET /artist`** - Returns list of artists

* **`GET /track`** - Returns list of music tracks

### Parameters

* `q` : Query string
* `limit` : Number of results to return
    * Optional
    * Default = 20
    * Max = 50

### Example

Request

    GET api/v1/search/track?q=search-term&limit=1

Response

 ```json
[
  {
    "id": 0,
    "name": "track name",
    "spotifyId": "track spotify-id"
  }
]
 ```

---
## Recommendation { api/v1/recommendation }

Get recommended tracks that match given tracks and albums. Max of 5 seeds total can be given at once

### Endpoints
* **`POST /`** - Get recommended tracks based on given seeds

### Parameters
* `limit` : Number of results to return
    * Optional
    * Default = 20
    * Max = 50

### Example
Request

    POST api/v1/recommendation?limit=1

```json
{
    "tracks" : ["spotifyTrackId1", "spotifyTrackId2"],
    "albums" : ["spotifyAlbumId1", "spotifyAlbumId2"]    
}
```

Response

```json
[
  {
    "id": 0,
    "name": "track name",
    "spotifyId": "track spotify-id"
  }
]
``` 

---