#### Presentation layer 
    - This layer is responsible for presenting the user interface to the user. 
    - It includes components like controllers, views, and templates.
#### Application layer 
    - This layer is responsible for implementing the use cases or business logic of the application. 
    - It includes components like services, use case controllers, and DTOs.
#### Domain layer
    - This layer is responsible for defining the business rules and domain objects of the application. 
    - It includes components like entities, value objects, and domain services.
#### Persistence layer
    - This layer is responsible for storing and retrieving the data from the database. 
    - It includes components like repositories, DAOs, and ORM frameworks.


#### When to use
1. Large-scale enterprise applications that have many different modules or subsystems.
2. Applications that need to support multiple interfaces, such as web and mobile.
3. Applications that require a high degree of flexibility and modularity, where individual components can be easily swapped out or replaced.
4. Applications that require strict separation of concerns, with clear boundaries between different layers of the system.

#### When to avoid
1. **Small projects**: For small projects with simple requirements, a layered architecture may introduce unnecessary complexity.
2. **Projects with frequent changes**: If a project requires frequent changes, a layered architecture may make it difficult to modify the application as changes to one layer may affect other layers.
3. **Projects with changing requirements**: If the requirements of a project are not well understood or are likely to change frequently, a layered architecture may not be the best approach as it can be difficult to modify the application to accommodate changing requirements.
4. **Projects that require high performance**: A layered architecture may introduce overhead in terms of processing time and memory usage, which can impact the performance of the application.
5. **Projects with low complexity**: For applications with low complexity, a layered architecture may be overkill and may not provide any significant benefits over a simpler architecture.