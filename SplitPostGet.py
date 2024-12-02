from nifiapi.flowfiletransform import FlowFileTransform, FlowFileTransformResult
from nifiapi.properties import PropertyDescriptor, PropertyDependency, ExpressionLanguageScope, StandardValidators

POST_KEYWORD = 'POST'
GET_KEYWORD = 'GET'

class SplitGetPostData(FlowFileTransform):
    class Java:
        implements = ['org.apache.nifi.python.processor.FlowFileTransform']

    class ProcessorDetails:
        version = '0.0.1-SNAPSHOT'
        description = 'Processor for Custom Text Split, based on POST and GET'
        tags = ['text', 'split', 'python', 'custom']

    def __init__(self, **context):
        pass


    def transform(self, context, flowfile):
        content = flowfile.getContentsAsBytes().decode('utf-8')
        '''SPLIT LOGIC THERE'''
        return FlowFileTransformResult(relationship='splitted',contents=...,attributes=...)
