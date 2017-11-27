# Spring Cloud Dataflow Demo

There are two supported ways to run this demo: in a hosted environment, or in your own environment.

* **Hosted**. The Spring Cloud Data Flow Demo can be run in the orgs **S1Pdemo14**, **S1Pdemo15**, and **S1Pdemo16** on Pivotal Web Services (If you need the credentials for these environments, contact Corby Page or Phil Berman).
* **Your own environment**. This demo comes with a concourse pipeline for installing all demo assets onto your own PCF environment. Instructions for this pipeline are found here: https://github.com/cpage-pivotal/scdf-demo/tree/master/ci

The instructions below will assume your are running in the hosted **S1Pdemo14** environment, but they are easily adapted to use the URLs, org, and space for whatever environment you choose.

# Prerequisites

1. Install the Spring Cloud Dataflow Shell on your local machine. You can download the 1.2.3 version of the shell here: https://repo.spring.io/libs-release/org/springframework/cloud/spring-cloud-dataflow-shell/1.2.3.RELEASE/spring-cloud-dataflow-shell-1.2.3.RELEASE.jar

   You can run the shell with `java -jar spring-cloud-dataflow-shell-1.2.3.RELEASE.jar`

2. Obtain your Twitter API credentials, if you don't already have them. You can generate these credentials at https://dev.twitter.com

The credentials you will need for this demo are:
* **Consumer Key**
* **Consumer Secret**
* **Access Token**
* **Access Token Secret**

# Deploy the Base Stream

Navigate to the **scdf** space in the demo environment. If the stream has not been deployed, you will see two apps running:

![scdf space](https://raw.githubusercontent.com/cpage-pivotal/scdf-demo/master/doc-images/screen1.png)

---

To access the Web UI, append /dashboard to the route for the dataflow-server. In this case, http://scdf-demo14.cfapps.io/dashboard:

![dashboard](https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen2.png?raw=true)

---

This gives a textual display of the available apps, but it is easier to demo with a graphical view. Click on the Streams header nav at the top of the screen, and then click on the "Create Stream" navtab next to Definitions:

![flo](https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen3.png?raw=true)

---

Now you can show how to visually compose a stream by wiring the twitterstream source, to the transform processor, to the log sink. When you are done, clicking the layout button will give a clearer view:

![layout](https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen4.png?raw=true)

---

When you click on each app in the stream, you will see a gear icon in the lower left, and an X icon in the lower right. Clicking on the gear icon will allow you to set the properties for the app. In the Twitterstream app, enter your credentials for the app properties:

![twitterstream-properties](https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen4-1.png?raw=true)

Also enter *tweets* for the Stream Name, and *en* for the Language.

Next, configure the transform processor by entering *payload.text* for the Expression. This will extract the field which contains message text from the JSON document returned by the Twitter API.

![transform-properties](https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen4-2.png?raw=true)

Click on the Create Stream button to save your changes:

![button](https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/button.png?raw=true)
---

Click back on the Definitions navtab to see the existing stream, tweets-demo14. Talk about the role of each of the apps in the stream:

![stream](https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen5.png?raw=true)

![stream-figure](https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen5-1.png?raw=true)

---

Click on the Deploy button, and then click Deploy on the subsequent screen to confirm deployment. This will start the process of launching the data microservices in Cloud Foundry that are needed to execute the stream:

![microservices](https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen6.png?raw=true)

Explain the benefits of having each of the microservices be managed by the platform. With no extra coding, you get manual or automatic scaling, routing, high availability, failover, logging and monitoring support for enterprise-level capabilities.

Using either the command line or apps manager, tail the logfile for the log sink app that was deployed by SCDF. You will see about 20 Tweets per second streaming live:

![tweet-feed](https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen6-1.png?raw=true)

# Deploy the Analysis Stream

Now we will create a branch off of this stream, known as a *tap* in SCDF, that concurrently performs natural language processing on the text of the tweets. We will use the open source Stanford Core NLP library, found here: http://stanfordnlp.github.io/CoreNLP/

As with any piece of legacy domain logic, it is easy to wrap the Core NLP library in a Spring Boot app, and deploy it as a processor into SCDF. This is the new flow we will create:

![nlp-figure](https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen6-2.png?raw=true)

The tap reads a copy of the message emitted by the transform processor, performs language processing, and outputs the data to Redis.

This time, we will use the command line to create the tap. Start the shell on your local machine, and point it at the SCDF environment by typing:

`dataflow config server http://scdf-demo14.cfapps.io`

If you type `stream list`, you will be able to see the currently deployed stream. Create the tap we described by typing:

`stream create lang-analysis --definition ":tweets.transform > nlp | redis"`

Deploy the stream by typing 

`stream deploy lang-analysis`

If you go back to the Apps Manager, you can see the two new microservices, nlp and redis, being deployed.

---

Now, let's look at visualization of the data that was persisted to Redis. Click on the route for the "bubble-chart-demo14" app:

![bubble](https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen7.png?raw=true)

Mouse over the individual bubbles to see the keywords that have been tabulated. You can use the "All Words" and "Parts of Speech" buttons to switch views of the data.

At the bottom of the screen, Refresh will update the view as the stream continues to run. Purge will flush the Redis store, and start tabulating from the beginning again.

**TIP**: If the stream runs for a long time, the visualization performance may eventually get sluggish as the Redis store gets very large. Simply hit the Purge button, and everything will be snappy again.

***IMPORTANT: At the end of your demo, please UNDEPLOY your streams. If you leave the streams running, the Redis store will grow very large, and make the next user unhappy.***
