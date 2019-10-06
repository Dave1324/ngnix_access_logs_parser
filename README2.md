## Adapting the system for scale

#### Source code adjustments
The code structure itself would require very minimal adjustment. 

#### Database architecture
As the database grows exponentially, there's a need to apply horizontal partitioning - or "sharding".
There are two primary ways in which to shard a database: Range, and hash. There are others but I'll
stick to the fundamentals. 

Range based sharding involves sharding data based on ranges of a given value.
Since our data is time sequential with very high cardinality, this is a good place to start. Since the
lions share of our interest is in data from the past month, the first step would be to have a "monthly" horizontal 
partition with the sharding range being the `created_at` column (we're talking about the `nginx_logfile_row`
table here). This may make for more efficient queries, but the new problem is; being so consistently time sequential, 
our "shard" is going to become a "database hotspot", meaning the data won't be very highly distributed 
relative to the I/O operational demand.
 
The solution to this problem lies with the second of the aforementioned sharding strategies: Key / hash 
based sharding. This involves choosing a unique valued column - such as the primary key itself, and plugging it into a hash function to 
determine which shard the data should go to. The advantage of this strategy is unpredictability. Since
the quality of hash functions are measured by an even, unique and random key->value distribution, 
this would allow us to create any number of **evenly distributed** sub-partitions within each shard, 
assigning records evenly across all of them. 

#### Parallel processing
For a multi-threaded or multi-instance 
environment optimistic locking would be necessary to ensure data integrity and prevent exploding transactions.
Then there are the routine aspects such as moving config values from static files to dynamic (typically system) variables.

#### Seperation of concerns
For a very large scale it might make sense to deperate the service into 2 APIs; one to aggregate and parse the log file data, and another to recieve incoming analytics requests.

#### Potential bottlenecks
The potentially problematic bottleneck here is of course the part where we're having
to iterate over every character of every line in every file in order to turn 
it into structured, actionable data. This is a very large amount of work and if not handled correctly 
could take unacceptable amounts of runtime. The solution is smart transaction mananagement to increase
the aggregational throughput, as well as making use of parallel processing - both in terms of using multiple threads in prallel, 
and using multiple instances (such as within an autoscaling group) in parallel.

#### Benchmarking

##### On single thread

- Avg lines per given nginx logfile: ~32,368
- Avg time per file: ~52 seconds
- Avg records per second: ~622

##### On multiple threads:

No improvement in my environment. 
This could be an optimization for 
environments more suited to parallel processing.