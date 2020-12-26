Configuration handling will be maintained here such as

The core engine orchestrates the application by 

Loading Tuple Configurations
invoking various actions depends on the configuration

By default, the core shall not have any specific implementation of the actions. It will invoke the individual modules in loosely coupled manner.  

The core operation  involves the below actions
0. load metadata configuration
1. validation of incoming data
2. find differential data to be inserted / updated and deleted.
3. perform the insertion / updates / deletion in the order.
4. invoke change log configuration

Also, the core module will provide various callback methods for customization

Transactional customization 

onCreate	- will be executed after validation of the request - for create request
onUpdate	- will be executed after validation of the request - for update request
postCreate	- will be executed after database updates - for create request
postUpdate	- will be executed after database updates - for update request
onDelete	- will be executed after validation of the request - for delete request
postCreate	- will be executed after database updates - for create request

Non-Transactional customization

preSave 	- will be executed before validation of the incoming request (non-transactional)
preDelete 	- will be executed before validation of the incoming request (non-transactional)
afterCreate - after all the database updates are committed - for create request
afterUpdate	- after all the database updates are committed - for update request
afterDelete	- after all the database updates are committed - for delete request



