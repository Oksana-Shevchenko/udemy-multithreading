Synchronization:
Two threads IncrementingThread and DecrementingThread incrementing and decrementing counter.
One thread can execute the critical section. 

Atomic operations:
Metrics aggregation application. Metrics class capture the samples.
BusinessLogic thread - add metrics sample.
MetricsPrinter thread - capture average time from metrics and prints it.

Data race:
Add data race example. Fix it with a help of volatile.