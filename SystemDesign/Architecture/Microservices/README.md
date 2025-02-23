1) Loosely Coupled: 
    Need to update the data  in 1 ms when updating the other
2) Code, Test, Deploy - Independently
3) Loose Communication overhead
4) Scale Independently: 
   if u scale 1 than other need to be scaled b/c of scaling of first. 
   otherwise breaks loose coupling. traffic should not scale in both of them together

## DomainDrivenDesign
#### Event storming
1. Extract Domain Events [User Register, User Login, User Logout, ...]
   - Domain events are simply objects that define some sort of event that occurs in the domain that domain experts care about.
   - **What happened?**
2. Sequence Events to avoid missing on any event (user register -> user -> login -> user sent message -> notification sent -> Notification seen -> user see message -> user logout)
3. Setting Up Commands
   - An operation that effects some change to the system (for example, setting a variable). An operation that intentionally creates a side effect.
   - **Why does it happen?**
   - Command is the cause that generates a domain event.
4. Find out Aggregates
   - An “aggregate” is a cluster of associated objects that we treat as a unit for the purpose of data changes.”
   - An aggregate is a collection of related value objects and entities that have a local responsibility and represent a specific business concept.
   - Try to define for each event/command the aggregate that they belong to.
5. Write your Policies
   - **“How does this happen?”**
   - Policies are written in this format: WHEN event THEN command.
6. Delimit Bounded Context
   - The delimited applicability of a particular model. BOUNDING CONTEXTS gives team members a clear and shared understanding of what has to be consistent and what can develop independently.
    User Management Bounded Context
        - User Register
        - user Login
        - user logout
    Message Bounded Context
        - message sent
        - message delivered
        - message deleted
    User Notification Message Bounded Context
        - Notification sent
        - Notification seen
7. Create Microservice for each Bounded Context

## Hexagonal Architecture
1. Domain Layer
   - Will be the holder of aggregates and value objects as well as an implementation of domain services and domain events.
   - The domain layer exposes its ports (API & SPI) to adapters on the application and infrastructure layers.

## Tactical Design
