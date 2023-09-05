## Project

- features:
  - API gateway pattern
  - decoupled services
  - data replication
  - message-oriented
  - Access token
    - Gateways handles authentication
    - each domain service handles authorization
  - GitHub Actions for continue integration

- improvements:
    - gateway
        - extract uri configuration into environment variables
        - circuit breaker
        - unit tests
    - auth
        - try refresh tokens
    - order
        - unit tests
    - overall:
        - pass environment variables through docker
        - caching
        - observability
            - APMs
        - create SAGA
        - CQRS
        - gRPC
        - dealing with concurrency
            - sharding?

- bug fixes:
    - e2e-tests
        - project not managed by parent project
    - auth
        - auth service should subscribe to user events