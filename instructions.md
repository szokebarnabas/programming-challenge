# Introduction

There are deployed a number of IoT sensors which emit JSON packets with the following schema:

	{
	  "$schema" : "http://json-schema.org/draft-04/schema#",
	  "title" : "Event",
	  "type" : "object",
	  "additionalProperties" : false,
	  "properties" : {
	    "eventId" : {
	      "type" : "string"
	    },
	    "sensorId" : {
	      "type" : "string"
	    },
	    "timestamp" : {
	      "type" : "integer"
	    },
	    "value" : {
	      "type" : "number"
	    }
	  },
	  "required" : [ "eventId", "sensorId", "timestamp", "value" ]
	}

where `id` is a **CUID** and `sensorId` is a **version 4 UUID**. An example payload looks like this:

    {
        "eventId" : "cj86g5ypk000004zvevipqxfn",
        "sensorId" : "fd0a635d-2aaf-4460-a817-6353e1b6c050",
        "timestamp" : "1506723249",
        "value" : "25.6734"
    }

An **Anomaly Detector** is a web service which accepts POST requests to `/api/event` of the above specified JSON event objects and returns a response indicating whether that reading is an anomaly or not. The response has the following schema:

	{
	  "$schema" : "http://json-schema.org/draft-04/schema#",
	  "title" : "Response",
	  "type" : "object",
	  "additionalProperties" : false,
	  "properties" : {
	    "eventId" : {
	      "type" : "string"
	    },
	    "sensorId" : {
	      "type" : "string"
	    },
	    "timestamp" : {
	      "type" : "integer"
	    },
	    "value" : {
	      "type" : "number"
	    },
	    "status" : {
	      "type" : "string",
	      "enum" : [ "NO_MODEL", "NO_ANOMALY", "ANOMALY", "ERROR" ]
	    },
	    "cause" : {
	      "type" : "string"
	    },
	    "message" : {
	      "type" : "string"
	    }
	  },
	  "required" : [ "eventId", "sensorId", "timestamp", "value", "status", "cause", "message" ]
	}

Here is an example response:

    {
        "eventId" : "cj86g5ypk000004zvevipqxfn",
        "sensorId" : "fd0a635d-2aaf-4460-a817-6353e1b6c050",
        "timestamp" : "1506723249",
        "value" : "25.6734"
        "status" : "ANOMALY",
        "cause" : "Upper Bound Threshold Detector",
        "message" : "Exceeds threshold"
    }

# The Challenge

This challenge has several parts, the goal is to achieve as much as you can within the allotted period of time. Remember to make your code as clean and readable as possible, consider using TDD, and include any instructions for compiling and running your application, as well as any specific instructions for testing the behaviour.

**TIP**: if you are familiar with any reactive or event driven frameworks you may find they provide a slight advantage in later parts of this challenge

## Part 0 

Fork this repository and rename this file to `instructions.md`. Include all your own build and running information in the `README.md` file.

For each of the following parts of the challenge please create a new branch from the last one so that the progress of your development can be tracked more easily. Also commit as often as you feel appropriate.

## Part 1

Using your build tool of choice and framework of choice, build an anomaly detector which simply logs each event received and returns a response like this:

    {
        "eventId" : "cj86g5ypk000004zvevipqxfn",
        "sensorId" : "fd0a635d-2aaf-4460-a817-6353e1b6c050",
        "timestamp" : "1506723249",
        "value" : "25.6734"
        "status" : "NO_MODEL",
        "cause" : "",
        "message" : ""
    }

except with the appropriate values for the event received.

## Part 2 

In order to detect anomalies we apply the in-coming data to **models** which are defined by configuration files read on startup of the application.

A configuration file specifies a specific sensor, the name of the model to apply to apply to that sensor, and any other configuration the model needs, e.g. a threshold value.

Implement a simple model which returns `NO_ANOMALY` if the incoming value for that sensor is **below** a given threshold value, and `ANOMALY` if the value is **above** the threshold. The configuration file might look like:

	{
        "sensorId" : "fd0a635d-2aaf-4460-a817-6353e1b6c050",
	     "model": "UpperBoundThresholdAnomalyDetector",
	     "threshold": "27.0"
	}
	
## Part 3

At this point we want to extend our anomaly detection capabilities, add another model that has a window of size X which is configurable and stores the X most recent events, and a threshold. Every time a new event arrives the detector takes the average of all the events values and if the average is above the threshold resturns `ANOMALY` otherwise it returns `NO_ANOMALY`
