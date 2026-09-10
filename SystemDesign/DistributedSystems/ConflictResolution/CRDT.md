## Example
- Amazon’s shopping cart is an excellent example of CRDT. 
  When a user adds two items (A & B) to the cart, these two operations
  of adding A & B can be done on any node and with any order, as the end
  result is the two items are added to the cart. (Removing from the shopping
  cart is modeled as a negative add.) 
- The idea that any two nodes that have
  received the same set of updates will see the same end result is called strong
  eventual consistency. 
- Riak has a few built-in [CRDTs]((https://docs.riak.com/riak/kv/2.2.0/developing/data-types/).). 