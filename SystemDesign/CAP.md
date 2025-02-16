# CAP
Whenever there is network failure you should get either consistency or availability

## Consistency
- whenever requests goes to any node, client sees same data 
- Strong Consistency: whenever reading you should get value of most recent write
- Weak Consistency: some read operations may not see updated value
- Eventual Consistency: latest data seen after some amount of time
## Availability
- Success response to each request
- achieved through replication
## Partition Tolerance
- Despite network fault the system should operate.


## CA
## CP
## AP



# Links
[CAP Conjecture](https://web.archive.org/web/20190629112250/https://www.glassbeam.com/sites/all/themes/glassbeam/images/blog/10.1.1.67.6951.pdf) - Consistency, Availability, Parition Tolerance cannot all be satisfied at once
[CAP Twelve Years Later](https://www.infoq.com/articles/cap-twelve-years-later-how-the-rules-have-changed): How the "Rules" Have Changed - Eric Brewer expands on the original tradeoff description