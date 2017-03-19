# Concourse Pipeline Instructions

This Concourse Pipeline will install all assets needed to run the SCDF demo. It will push the SCDF server and the visualizer application, create and bind all needed services, and register SCDF apps used by the demo.

## Prerequisites

Your target CF environment must have the following services available in the marketplace:
* MySQL
* Redis
* RabbitMQ

## Executing the Pipeline

Clone this repo onto your local environment, and edit **ci/consumer-vars.yml**. This is where you will enter all the configuration information specific to your environment.

Log into your Concourse environment with fly. From the ci directory, execute the following command:

`fly -t [alias] set-pipeline -p scdf-demo -c pipeline.yml -l consumer-vars.yml`

Now, navigate to your newly created pipeline in the Concourse dashboard:

![](https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/pipe1.png)

Click on the deploy-scdf-demo job:

![](https://github.com/cpage-pivotal/scdf-demo/blob/master/doc-images/pipe2.png)

Then click on the + sign to start a build. The build should complete in under 15 minutes.

Now you are ready to run the demo! Follow the instructions on the main page: https://github.com/cpage-pivotal/scdf-demo
