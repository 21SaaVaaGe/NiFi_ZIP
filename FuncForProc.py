import json
import boto3
import io
import zipfile

s3 = boto3.client('s3')

def process_flowfile(flowfile):
    content = flowfile.read().decode('utf-8')
    data = json.loads(content)
    
    with io.BytesIO() as zip_buffer:
        with zipfile.ZipFile(zip_buffer, 'w', zipfile.ZIP_STORED) as zip_file:
            for item in data:
                s3_key = item['s3.key']
                s3_bucket = item['s3.bucket']
                filename = item['filename']
                unique_filename = item['unique_filename']
                
                s3_object = s3.get_object(Bucket=s3_bucket, Key=s3_key)
                file_content = s3_object['Body'].read()
                
                zip_file.writestr(unique_filename, file_content)
    
        return zip_buffer.getvalue()
