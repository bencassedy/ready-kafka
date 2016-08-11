# ready-kafka
## Overview
The goal of this project is to provide a ready-to-use Kafka client library, including a consumer and a producer,
written in Scala. The motivation for this project comes from the desire to avoid much of the boilerplate
associated with teeing up a Kafka client in a Java-esque language, i.e., the `Properties()` incantations,
implementing thread executors for each partition, looping, adding a shutdown hook, and the the like.

## IMPORTANT: The Project Name
The project name is a quote from a scene in the movie Spaceballs, when, immediately before
initializing the Mega-Maid metamorphosis, Dark Helmet asks Colonel Sandurz, "Ready, Kafka?"

I found this name devastatingly appropriate for several reasons:
 - Spaceballs
 - Kafka
 - The metamorphosis, or transformation, of something into a more useful tool
 - The name itself, which suggests a ready-to-use kafka client library
 - BONUS: The Producers also has a subtly awesome Kafka/Metamorphosis reference