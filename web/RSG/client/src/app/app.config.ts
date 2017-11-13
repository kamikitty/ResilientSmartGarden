/* stores application config variables (like the api enpoint url) in a single place that's easily imported into any component
used by the Angular 2 User Service and Authentication*/

export const appConfig = {
	apiUrl: 'API Url',
	apiLogin: 'http://localhost:3001/login',
	apiRegister: 'http://localhost:3001/register',
	apiGetGardens: 'https://45c20831-7a16-4517-8b95-d0bc95a702fd.mock.pstmn.io/garden',
	apiAddGarden: 'https://45c20831-7a16-4517-8b95-d0bc95a702fd.mock.pstmn.io/add',
	apiSensors: 'https://45c20831-7a16-4517-8b95-d0bc95a702fd.mock.pstmn.io/readings',
	apiGetPlants: 'https://87efd082-2aca-4bb4-aac6-b1f66e94e8c8.mock.pstmn.io/get_plants',
	apiGetPlantsName: 'http://localhost:3001/get_plant_name',
	apiGetCompanion: 'http://localhost:3001/get_companion',
	apiGetEnemy: 'http://localhost:3001/get_enemy'
};
