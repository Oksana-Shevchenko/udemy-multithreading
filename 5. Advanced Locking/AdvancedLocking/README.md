Reentrant lock:
Dashboard of crypto prices.
Application have 2 threads. UI thread - show animation, response to mouse events, display current prices of assets.
PricesUpdater - background thread. Get up-to-date prices for all assets periodically.
PricesContainer - shared resource between UI tread and background thread.

Reentrant read-write lock:
Inventory Database
One writer thread add or remove items.
7 Reader threads to get count of items in price range method.