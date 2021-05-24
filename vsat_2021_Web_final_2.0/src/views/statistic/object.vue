<template>
  <div class="app-container">
    <h2 class="no-margin-top">Thống kê theo đối tượng</h2>
    <div class="filter-container">
        <el-row :gutter="20" >
        <div >
            <template>            
              <div class="block">
                  <el-select  v-model="optionDate" filterable  @change="changeOption()" style="width :120px;vertical-align: center;">
                    <el-option v-for="item in list_option" :key="item.id" :label="item.value" :value="item.id" >                          
                    </el-option>
                   </el-select>
              </div>
              <div class="block" v-show="optionDate == 0" >
                <div class="block">
                <el-date-picker v-model="query.startDate"  size="small" format="dd-MM-yyyy HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss"
                :editable="false" type="datetime" style="width :200px;vertical-align: center;" ></el-date-picker>                
                
                </div>    
                <div class="block">
                <el-date-picker v-model="query.endDate"  size="small" format="dd-MM-yyyy HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss"
                :editable="false" type="datetime" style="width :200px;vertical-align: center;" ></el-date-picker>         
                </div>    
              </div>    

              <div class="block" v-show="optionDate == 1">
                <div class="block" >
                  <span style="margin-left:20px">Từ tuần : </span>
                  <el-select v-model="startWeek"  size="small" @change="changeStartWeekOption()"  style="width :80px;vertical-align: center;">
                      <el-option v-for="item in list_week" :key="item" :label="item" :value="item" >                          
                      </el-option>
                  </el-select>                               
                </div> 
                <span style="margin-right:3px" >/ </span>    
                <div class="block">
                  <el-select v-model="startWeekYear"  size="small"  style="width :80px;vertical-align: center;">
                      <el-option v-for="item in list_year" :key="item" :label="item" :value="item" >                          
                      </el-option>
                  </el-select>        
                </div> 

                <div class="block" >
                  <span style="margin-left:20px">Đến tuần : </span>
                  <el-select v-model="endWeek"  size="small"  style="width :80px;vertical-align: center;">
                      <el-option v-for="item in list_week" :key="item" :label="item" :value="item" >                          
                      </el-option>
                  </el-select>                               
                </div> 
                <span style="margin-right:3px" >/ </span>    
                <div class="block">
                  <el-select v-model="endWeekYear"  size="small"  style="width :80px;vertical-align: center;">
                      <el-option v-for="item in list_year" :key="item" :label="item" :value="item" >                          
                      </el-option>
                  </el-select>        
                </div>

              </div>    

              <div class="block" v-show="optionDate == 2">
                <div class="block" >
                  <span style="margin-left:20px">Từ tháng : </span>
                  <el-select v-model="startMonth"  size="small" @change="changeStartWeekOption()"  style="width :80px;vertical-align: center;">
                      <el-option v-for="item in list_month" :key="item" :label="item" :value="item" >                          
                      </el-option>
                  </el-select>                               
                </div> 
                <span style="margin-right:3px" >/ </span>    
                <div class="block">
                  <el-select v-model="startMonthYear"  size="small"  style="width :80px;vertical-align: center;">
                      <el-option v-for="item in list_year" :key="item" :label="item" :value="item" >                          
                      </el-option>
                  </el-select>        
                </div> 

                <div class="block" >
                  <span style="margin-left:20px">Đến tháng : </span>
                  <el-select v-model="endMonth"  size="small"  style="width :80px;vertical-align: center;">
                      <el-option v-for="item in list_month" :key="item" :label="item" :value="item" >                          
                      </el-option>
                  </el-select>                               
                </div> 
                <span style="margin-right:3px" >/ </span>    
                <div class="block">
                  <el-select v-model="endMonthYear"  size="small"  style="width :80px;vertical-align: center;">
                      <el-option v-for="item in list_year" :key="item" :label="item" :value="item" >                          
                      </el-option>
                  </el-select>        
                </div> 
              </div> 

              <!-- ádasd -->                                   
              <el-button class="search" @click="handleFilter()" type="primary">Tìm kiếm</el-button> 
              <el-button :loading="downloadLoading" style="margin:0 0 20px 20px;" type="primary" icon="el-icon-document" @click="handleExport()">
                 Xuất Excel
              </el-button>
          </template>
        </div>
      </el-row>
    </div>
    <el-row>
      <el-col :span="24">
        <el-table
          v-loading="loading"
          :data="list"
          border
          fit
          highlight-current-row
          style="width: 100%"
        >
          <el-table-column align="center" label="#" width="100">
            <template slot-scope="scope">
              <span>{{
                (query.currentPage - 1) * query.rowsPerPage + scope.$index + 1
              }}</span>
            </template>
          </el-table-column>
          <el-table-column label="IP (Tên tàu) nguồn">
              <template slot-scope="scope">
                  <span>{{ scope.row.sourceIP }} <span v-show="scope.row.sourceName !== ''">(</span> {{scope.row.sourceName}}<span v-show="scope.row.sourceName !== ''">)</span></span>
              </template>
          </el-table-column>

          <el-table-column label="IP (Tên tàu) đích">
              <template slot-scope="scope">
                  <span>{{ scope.row.destIP }} <span v-show="scope.row.destName !== ''">(</span> {{scope.row.destName}}<span v-show="scope.row.destName !== ''">)</span> </span>
              </template>
          </el-table-column>

          <el-table-column label="Loại dữ liệu">
              <template slot-scope="scope">
                  <span>{{ scope.row.mediaTypeName }}</span>
              </template>
          </el-table-column>          

          <el-table-column label="Dung lượng (Bytes)" width="200">
              <template slot-scope="scope">
                  <span>{{ scope.row.totalData }}</span>
              </template>
          </el-table-column>

          <el-table-column label="Số lượng" width="100">
              <template slot-scope="scope">
                  <span>{{ scope.row.totalAmount }}</span>
              </template>
          </el-table-column>

          <el-table-column label="Ngày" width="100">
              <template slot-scope="scope">
                  <span>{{ scope.row.byDate | formatDateValueString }}</span>
              </template>
          </el-table-column>     


        </el-table>


        <pagination
          v-show="total > 10"
          :total="total"
          :page.sync="query.currentPage"
          :limit.sync="query.rowsPerPage"
          @pagination="getList"
        />
      </el-col>   
    </el-row>


  </div>
</template>



<script>
import Pagination from "@/components/Pagination";
import waves from "@/directive/waves"; // Waves directive
import Resource from "@/api/resource";
import { mapGetters } from 'vuex';
import { validString } from "@/utils";
import PieChart from '../dashboard/admin/components/PieChart.vue';

const dataSourceResource = new Resource("manager/source/get-all");
const statisticVesselResource = new Resource("statistic/vessel");


let today = new Date();
let day = checkZero(today.getDate() + '');
let month = checkZero((today.getMonth() + 1) + '');
let year = checkZero((today.getFullYear() + 1) + '');


export default {
  components: {
    Pagination,
    PieChart,
    //AdvancedSelect ,
  },
  directives: { waves },
  data() {
    return {
      renderComponent: true,
      downloadLoading: false,
      list: null,
      dataALL : null,
      dataSourceLst: null,
      total: 0,
      loading: true,
      optionDate:0,
      query: {
          currentPage: 1,
          rowsPerPage: 10,
          sort: "createdAt", 
          keyword: "",
          optionDate:0,
          startDate: today.getFullYear() + '-' +  month + '-' +  day + ' 00:00:00',
          endDate: getCurrentDateToStr(today),

      },
      queryForAll:{
          currentPage: 1,
          rowsPerPage: 10,
          sort: "createdAt", 
          keyword: "",
          optionDate:0,
          startDate: today.getFullYear() + '-' +  month + '-' +  day + ' 00:00:00',
          endDate: getCurrentDateToStr(today),

      },
      startWeek :null,
      endWeek:null,
      startWeekYear:null,
      endWeekYear:null,
      startMonth :null,
      endMonth:null,
      startMonthYear:null,
      endMonthYear:null,

      list_week :null,
      list_month:null,
      list_year :null,

      filterOption:{
          id: 0,
          value: "Theo ngày",
      },
      list_option: [
        {
          id: 0,
          value: "Theo ngày",
        },
        {
          id: 1,
          value: "Theo tuần",
        },
        {
          id: 2,
          value: "Theo tháng",
        },
      ],
      tempDataSource : null,

    };
  },
    
  computed: {
     ...mapGetters([
      'userId',
      'userName',
      'name',
      'avatar',
      'roles',
    ]),
  },
  created() {
    this.getList();
    this.setDateValueFirst();
  },

  methods: {
    
    async getList() {
      this.loading = true;
      this.query.keyword = this.query.keyword.trim();

      const { data, total } = await statisticVesselResource.listByPost(this.query);
      
      this.list = data;
      
      if (data) {
        this.total = total;
      }


      this.loading = false;
    }, 

    async setDateValueFirst(){
      var week = [];
      var setYear = [];
      var setMonth = [];
      for(var i = 0 ; i<=52;i++){
        week[i] = i + 1;
      } 
      for(var i = 0 ; i <= 11 ; i++){
        setMonth[i] = i + 1;
      }
      for(var i=0;i<=20;i++){
        setYear[i] = year - i-1;
      }
      this.list_week = week;
      this.list_year = setYear;
      this.list_month = setMonth;
    },

    changeOption(){
      console.log("changeOption");
      if(this.optionDate == 0){
          this.query.startDate = today.getFullYear() + '-' +  month + '-' +  day + ' 00:00:00',
          this.query.endDate = getCurrentDateToStr(today);
      }
      if(this.optionDate == 1){
        this.getWeekNumber();
      }
      if(this.optionDate == 2){
        this.startMonth = today.getMonth() +1;
        this.endMonth = today.getMonth() +1;
        this.startMonthYear = today.getFullYear();
        this.endMonthYear = today.getFullYear();
      }

      
    },

    changeStartWeekOption(){
      console.log("changeStartWeekOption");
    },

    getWeekNumber() {
        // Copy date so don't modify original
        var d = new Date(Date.UTC(today.getFullYear(), today.getMonth(), today.getDate()));
        // Set to nearest Thursday: current date + 4 - current day number
        // Make Sunday's day number 7
        d.setUTCDate(d.getUTCDate() + 4 - (d.getUTCDay()||7));
        // Get first day of year
        var yearStart = new Date(Date.UTC(d.getUTCFullYear(),0,1));
        // Calculate full weeks to nearest Thursday
        var weekNo = Math.ceil(( ( (d - yearStart) / 86400000) + 1)/7);
        console.log("mrd weekNo : " + weekNo);
        this.startWeek = weekNo;
        this.endWeek = weekNo;
        this.startWeekYear = today.getFullYear();
        this.endWeekYear = today.getFullYear();
        // Return array of year and week number
    },       

    handleFilter(){
      if(this.optionDate == 1){
        this.query.startDate = commonGetDateFromYearAndWeek(this.startWeek, this.startWeekYear, "from")
        console.log("mrd startDate: " + this.query.startDate)
        this.query.endDate = commonGetDateFromYearAndWeek(this.endWeek, this.endWeekYear, "end")
        console.log("mrd endDate: " + this.query.endDate)

      }else if(this.optionDate == 2){
        this.query.startDate = commonGetDateFromYearAndMonth(this.startMonth, this.startMonthYear,"from")
        this.query.endDate = commonGetDateFromYearAndMonth(this.endMonth, this.endMonthYear, "end")
        console.log("startDate = " + this.query.startDate +   " ,month = " + this.startMonth + ", year = " + this.startMonthYear);
        console.log("EndDate = " + this.query.endDate +   " ,month = " +  this.endMonth + ", endMonthYear : " + this.endMonthYear);
      }
      
      if(this.query.startDate > this.query.endDate){
        this.$notify({
          title: "Có lỗi xảy ra",
          message: "Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc!",
          type: "error",
        }); 
        return;
      }

      this.query.currentPage = 1;
      this.getList();
    },
    async handleExport(){
      this.downloadLoading = true
      this.queryForAll.rowsPerPage = this.total
      this.queryForAll.startDate = this.query.startDate;
      this.queryForAll.endDate = this.query.endDate;
      const {data} = await statisticVesselResource.listByPost(this.queryForAll);
      this.dataALL = data;
      console.log(data.length)
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['IP nguồn', 'Tên tàu nguồn','IP đích', 'Tên tàu đích', 'Loại dữ liệu', 'Dung lượng (Bytes)','Số lượng', 'Ngày']
        const filterVal  = ['sourceIP','sourceName','destIP','destName', 'mediaTypeName', 'totalData','totalAmount', 'byDate']

        const listALL = this.dataALL;  
        const data = this.formatJson(filterVal, listALL);
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename:'Baocao',
          autoWidth: true, //Optional
          bookType: 'xlsx' //Optional
        })
        this.downloadLoading = false
        })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },

  }
};
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;
  .card-panel-col {
    margin-bottom: 30px;
  }
  .card-panel {
    padding-bottom: 25px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #000;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, 0.05);
    border-color: rgba(0, 0, 0, 0.05);
    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }
      .icon-people {
        background: #40c9c6;
      }
      .icon-message {
        background: #36a3f7;
      }
      .icon-money {
        background: #f4516c;
      }
      .icon-shopping {
        background: #34bfa3;
      }
    }
    .icon-people {
      color: #40c9c6;
    }
    .icon-message {
      color: #36a3f7;
    }
    .icon-money {
      color: #f4516c;
    }
    .icon-shopping {
      color: #34bfa3;
    }
    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }
    .card-panel-icon {
      float: left;
      font-size: 48px;
    }
    .card-panel-description {
      float: left;
      font-weight: bold;
      margin: 25px 0 20px 15px;
      .card-panel-text {
        line-height: 18px;
        color: #000;
        font-size: 16px;
        margin-bottom: 12px;
      }
      .card-panel-num {
        font-size: 20px;
      }
    }
    .card-panel-detail {
      float: left;
      width: 100%;
      padding: 0 15px;
      margin-bottom: 5px;
    }
    .el-tag {
      width: 100% !important;
      text-align: center !important;
    }
  }
}
.no-margin-top {
  margin-top: 0;
}
.search {
  margin-left: 15px;
}
.block {
  display: inline-block;
  //float: left;
  margin-right: 10px;
}
.blue {
  color: green;
}
.red {
  color: red;
}
.add-value {
  text-align: right;
}
.list-detail .item {
  margin-bottom: 10px;
  .label {
    display: inline-block;
    width: 100px;
  }
  img {
    max-width: 100%;
    display: inline-block;
  }
}

.el-input-number{
  width: 100%;
}
.card-body {
    flex: 1 1 auto;
    min-height: 1px;
    padding: 1.25rem;
    margin-left: 18px;
    border: 1px solid #e6ebf5;
}
</style>
<style lang="scss">
.form-value {
  .el-input__inner {
    margin-bottom: 10px;
  }
}
.el-input--small .el-input__inner {
  height: 36px;
}
.claim td .el-button {
  margin-left: 0;
}
.el-form-item--medium .el-form-item__content, .el-form-item--medium .el-form-item__label{
  line-height: 15px;
}
.el-textarea__inner {
    height: 200px;
}

</style>