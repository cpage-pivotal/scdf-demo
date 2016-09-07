# Spring Cloud Dataflow Demo

The Spring Cloud Data Flow Demo can be run in the orgs **S1Pdemo14**, **S1Pdemo15**, and **S1Pdemo16** on Pivotal Web Services (If you need the credentials for these environments, contact Corby Page or Phil Berman). I will use the URLs for **S1Pdemo14** below, but you can change the number 14 to 15 or 16 to run in the other environments.

# Prerequisites

1. Install the Spring Cloud Dataflow Shell on your local machine. You can download the 1.0.0 version of the shell here: https://repo.spring.io/libs-release/org/springframework/cloud/spring-cloud-dataflow-shell/1.0.0.RELEASE/spring-cloud-dataflow-shell-1.0.0.RELEASE.jar

You can run the shell with `java -jar spring-cloud-dataflow-shell-1.0.0.RELEASE.jar`

2. Obtain your Twitter API credentials, if you don't already have them. You can generate these credentials at https://dev.twitter.com

The credentials you will need for this demo are:
**Consumer Key**
**Consumer Secret**
**Access Token**
**Access Token Secret**

Navigate to the **scdf** space in the demo environment. If the stream has not been deployed, you will see two apps running:

![scdf space]
(https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen1.png?raw=true)

---

To access the Web UI, append /dashboard to the route for the dataflow-server. In this case, http://scdf-demo14.cfapps.io/dashboard:

![dashboard]
(https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen2.png?raw=true)

---

This gives a textual display of the available apps, but it is easier to demo with a graphical view. Click on the Streams header nav at the top of the screen, and then click on the "Create Stream" navtab next to Definitions:

![flo]
(https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen3.png?raw=true)

---

Now you can show how to visually compose a stream by wiring one source, to one or more processors to one sink. When you are done, clicking the layout button will give a clearer view:

![layout]
(https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen4.png?raw=true)

---

Click back on the Definitions navtab to see the existing stream, tweets-demo14. Talk about the role of each of the apps in the stream:

![stream]
(https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen5.png?raw=true)

---

Click on the Deploy button, and then click Deploy on the subsequent screen to confirm deployment. This will start the process of launching the data microservices in Cloud Foundry that are needed to execute the stream:

![microservices]
(https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen6.png?raw=true)

Explain the benefits of having each of the microservices be managed by the platform. With no extra coding, you get manual or automatic scaling, routing, high availability, failover, logging and monitoring support for enterprise-level capabilities.

---

Now, let's look at visualization of the data that was persisted to Redis. Click on the route for the "bubble-chart-demo14" app:

![bubble]
(https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/screen7.png?raw=true)

Mouse over the individual bubbles to see the keywords that have been tabulated. You can use the "All Words" and "Parts of Speech" buttons to switch views of the data.

At the bottom of the screen, Refresh will update the view as the stream continues to run. Purge will flush the Redis store, and start tabulating from the beginning again.

**TIP**: If the stream runs for a long time, the visualization performance may eventually get sluggish as the Redis store gets very large. Simply hit the Purge button, and everything will be snappy again.


