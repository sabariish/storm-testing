StormTopologyTesting
---------------------
THis project is inteted to demonstrate Storm topology local testing and integration testing.
This package includes two integration test cases which requires local storm cluster running on localhost 
(zookeeper,numbus,supervisor,logviewer). Also add required storm config parameters into storm.yaml file.

This package also includes one stoem topology test case which requires mocked datasource for spout. This test cases uses LocalCluster to simulate actual storm cluster.

This package also includs JUnit test case runner which runs JUnit test case and prints test report at the end.