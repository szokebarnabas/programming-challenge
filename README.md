# anomaly detector

##How to run it:

1) Build a fat jar
```
sbt assembly
```

2) Set the path of the configuration file
```
export MODEL_CONFIG_PATH=simpleConfig.json
```

3) Run the application
```
java -jar anomalydetector.jar
```

###Configuration file format

```
{
  "modelMapping": [
    {
      "sensorId": "1354978e-711f-4f26-bd96-6a6735064076",
      "model": "UpperBoundThresholdAnomalyDetector",
      "threshold": 27.0
    },
    {
      "sensorId": "51971af0-8d3d-43b9-b453-bece5ed6eb04",
      "model": "UpperBoundThresholdAnomalyDetector",
      "threshold": 43.0
    },
    {
      "sensorId": "863beb58-d820-4512-888e-6383bbe1ef77",
      "model": "MovingWindowThresholdAnomalyDetector",
      "threshold": 33.0,
      "modelParams": {
        "windowSize": "5",
        "foo": "bar"
      }
    },
    {
      "sensorId": "32db86fa-e853-4080-94d4-d6125ee028b3",
      "model": "MovingWindowThresholdAnomalyDetector",
      "threshold": 33.0,
      "modelParams": {
        "windowSize": "5",
        "foo": "bar"
      }
    }
}
```
###Example

```
Post localhost:9000/api/event
```
Body:
```
{
    "eventId" : "cj86g5ypk000004zvevipqxfn",
    "sensorId" : "b46dfc91-7a62-4fcc-966a-862dcb053af3",
    "timestamp" : 1506723249,
    "value" : 3000.0
}
```

Response: 200 OK
```
{
    "eventId": "cj86g5ypk000004zvevipqxfn",
    "sensorId": "b46dfc91-7a62-4fcc-966a-862dcb053af3",
    "timestamp": 1509370754524,
    "value": 3000,
    "status": "ANOMALY",
    "cause": "Moving Windows Average Threshold Detector",
    "message": "Exceeds threshold"
}
```

