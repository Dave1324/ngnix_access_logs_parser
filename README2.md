## Adapting the system for scale

#### Source code adjustments
The code structure itself would require very minimal adjustment. 

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