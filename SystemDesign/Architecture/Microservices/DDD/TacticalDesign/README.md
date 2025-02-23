- It involves breaking down the domain model into smaller, cohesive and loosely coupled 
- units of functionality called bounded contexts, aggregates, entities, value objects, and domain services. 

- how we’re building it

### Implementing Generic and Supporting Sub-Domains
#### Transaction Script
Organizes business logic by procedures where each procedure handles a single request from the presentation

#### Active Records
An object that wraps a row in a database table or view, encapsulates the database access, and adds domain logic on that data


### Implementing Core Sub-Domain
#### Domain Model
A domain model is an object model of the domain that incorporates both behavior and data.

#### ElementsOfDomainModel Or DDD Tactical Patterns
1. **Value Objects**: Value objects are objects that have no unique identity but are defined by their attributes. Immutable.
2. **Entities**:
    - Entities are objects that have **unique identities** and lifecycles and are **mutable** over time.
    - Value objects can describe the properties of an entity, which means they can be the fields of an entity.
3. **Aggregates**: 
   - Aggregates are clusters of related objects that are treated as a single unit of work. 
   - It regroups (aggregates) entities and value objects that belong to the same business logic boundary.
   - An aggregate is mutable, and its state should be altered only by methods within its public interface.
   - It should be small to include only objects required to keep it in a consistent state.
   - It should ensure that all the changes to the aggregates’ data are done as one atomic transaction.
4. **Aggregate Root**:
   - It’s the public interface of an aggregate. 
   - It contains behaviours, which are commands that can modify the state of an aggregate when executed. 
   - It’s a way of communicating the aggregate with the external world.
5. **Domain Events**: 
   - Are important events that occur within the domain that other parts of the system may need to know about.
   - Are another way to communicate the aggregate with the external world.
   - An aggregate also subscribes to receive external domain events and executes its business logic as a response to these events.
6. **Domain Services**: 
   - Domain services are operations or behaviors that apply to the domain as a whole rather than to a specific entity or value object.
   - These are stateless objects that implement a business logic that seems relevant to multiple aggregates or that do not belong to any value object or aggregate.
5. **Bounded Contexts**: Bounded contexts define a clear boundary around a specific part of the domain model and the ubiquitous language that goes with it.
5. **Services**: Services are operations or behaviors that do not naturally fit within a single entity or value object.
8. **Factories**: Factories are used to create complex objects or aggregates that may require multiple steps or complex logic. 
9. **Repositories**: Repositories are used to abstract the storage and retrieval of aggregates and entities.