<template>
  <el-table
    v-loading="loading"
    :data="list"
    style="width: 100%;padding-top: 15px;"
  >
    <el-table-column label="Order #" min-width="200">
      <template slot-scope="scope">
        {{ scope.row && scope.row.order_no | orderNoFilter }}
      </template>
    </el-table-column>
    <el-table-column label="Price" width="195" align="center">
      <template slot-scope="scope">
        ¥{{ scope.row && scope.row.price | toThousandFilter }}
      </template>
    </el-table-column>
    <el-table-column label="Status" width="100" align="center">
      <template slot-scope="scope">
        <el-tag :type="scope.row && scope.row.status | statusFilter">
          {{ scope.row && scope.row.status }}
        </el-tag>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>

export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        success: 'success',
        pending: 'danger',
      };
      return statusMap[status];
    },
    orderNoFilter(str) {
      return str;
    },
  },
  data() {
    return {
      list: [{ order_no: '1', price: '2', status: 'pending' }],
      loading: true,
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
    },
  },
};
</script>
