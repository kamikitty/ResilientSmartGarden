/* stores application config variables (like the api enpoint url) in a single place that's easily imported into any component
used by the Angular 2 User Service and Authentication*/

export const appConfig = {
	apiUrl: 'API Url',
	apiLogin: 'Address to Login API',
	apiRegister: 'Address to Register API',
	apiGetGardens: 'Address to Garden Update API',
	apiAddGarden: 'Address to Garden Add API',
	apiSensors: 'Address to Sensor API'
};
