import React from 'react';
import { Modal, Form, Input } from 'antd';
import { OrgData } from '../data';
import { existOrgId } from '../service';

interface OrgFormProps {
  title: string;
  visible: boolean;
  onCancel(): void;
  onSubmit(value: OrgData): void;
  info: Partial<OrgData>;
}

const formItemLayout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 7 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 12 },
    md: { span: 10 },
  },
};

const OrgForm: React.SFC<OrgFormProps> = props => {

  const [form] = Form.useForm();
  const { title, visible, onCancel, onSubmit, info } = props;

  const handleSubmit = (values: OrgData) => {
    onSubmit(values);
    form.resetFields();
    onCancel();
  }

  return (
    <Modal
      maskClosable={false}
      title={title}
      visible={visible}
      onOk={() => form.submit()}
      onCancel={() => {
        form.resetFields();
        onCancel();
      }}
    >
      <Form  {...formItemLayout} form={form} initialValues={info} onFinish={handleSubmit}>
        <Form.Item label="机构ID" name="id"
          rules={[
            {
              required: true,
              whitespace: true,
              message: '请输入机构ID',
            },
            {
              validator: async (_, value) => {
                if (value === "")
                  return Promise.resolve();
                if (value === info.id)
                  return Promise.resolve();
                if (value.includes(" "))
                  return Promise.reject('机构ID不能包含空格');
                return existOrgId(value).then((result: boolean) => result ? Promise.reject('机构ID已存在') : Promise.resolve())
              },
            },
          ]}
        >
          <Input placeholder="请输入" />
        </Form.Item>
        <Form.Item label="机构名称" name="name"
          rules={[
            {
              required: true,
              message: '请输入机构名称',
            },
          ]}
        >
          <Input placeholder="请输入" />
        </Form.Item>
      </Form>
    </Modal>
  );
}

export default OrgForm;
