Matrix multiplication:
MatricesReaderProducer thread - read pair of matrices from the file.
MatricesMultiplierConsumer thread - multiply a pair of matrices and safe result to another file.
Between MatricesReaderProducer and MatricesMultiplierConsumer exists ThreadSafeQueue.

ThreadSafeQueue has wait and notify methods.