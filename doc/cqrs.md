# CQRS: Origin, Pros and Cons

## Origin

Command Query Responsibility Segregation was a term created by Greg Young and became popular among the development community around 2010.

It came originally from the CQS (Command-query separation) principle, devised by Bertrant Meyer and states that every method should either be a command that performs a data state alteration or a query that returns data, but never both.

Young took that concept and applied to a service context: commands are requests that intend to change server state. Some of the suitable HTTP methods would be `POST`, `PUT`, `DELETE` or `PATCH`. Queries, on the other hand, are requests that look for data retrieval only (e.g. `GET` method), not affecting the system state in any way - with some exceptions such as logging.

## Pros
Once that separation concept is well-established, we might take advantage of that in order to gain performance by segregating command and query interfaces, leaving space for different - and hopefully more efficient - databases and even different services for a more grained scalability according to what's been most demanded. It also improves separation of concern, simplifying the command and query models.

## Cons
One to other hand, this pattern adds a lot of complexity and management overhead, since there's a number increase of services and databases. This increased number also adds the potential of code duplication and makes the views eventually consistent, as there's a replication lag. With that in mind, this pattern should only be used on very specific portions of a system.