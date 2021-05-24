<template>
  <div class="app-container">
    <h2 class="no-margin-top">Thống kê dung lượng theo nguồn</h2>
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

              <div class="block">
                  <el-select  v-model="query.dataSource" filterable  @change="changeOptionDataSource()" placeholder="Nguồn dữ liệu" clearable style="vertical-align: center;">
                    <el-option v-for="item in dataSourceLst" :key="item.id" :label="item.sourceName" :value="item.id">            
                    </el-option>
                   </el-select>
              </div>                                    
              <el-button class="search" @click="handleFilter()" type="primary">Tìm kiếm</el-button> 
          </template>
        </div>
      </el-row>
    </div>
    <el-row>
      <el-col :span="15">
        <el-table
          v-loading="loading"
          :data="list"
          border
          fit
          highlight-current-row
          style="width: 100%"
        >
          <el-table-column align="center" label="#" width="50">
            <template slot-scope="scope">
              <span>{{
                (query.currentPage - 1) * query.rowsPerPage + scope.$index + 1
              }}</span>
            </template>
          </el-table-column>
          <el-table-column label="Tên nguồn">
              <template slot-scope="scope">
                  <span>{{ scope.row.dataSourceName }}</span>
              </template>
          </el-table-column>

          <el-table-column label="Ngày" width="100">
              <template slot-scope="scope">
                  <span>{{ scope.row.byDate | formatDateValueString }}</span>
              </template>
          </el-table-column>

          <el-table-column label="Dung lượng dữ liệu" width="200">
              <template slot-scope="scope">
                  <span>{{ scope.row.totalData | formatCapacity }}</span>
              </template>
          </el-table-column>

          <el-table-column label="Số bản tin" width="100">
              <template slot-scope="scope">
                  <span>{{ scope.row.totalTopic }}</span>
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
      <br/><br/>
      <template>
            
        <LineChart
                :data="lineChartData"
                :options="lineOptions"
                v-if="renderComponent"
              />
           
      </template>

      </el-col>
      <el-col :span="9">
        <el-row>
           <div class="scroll" style="height:1000px;overflow-x:hiden;overflow-y:auto">
              <div id="card-body" class="card-body"  v-loading="loadingPie">  
                <template>                      
                  <PieChart
                      :data="pieChartData"
                      :options="pieOptions"
                      v-if="renderComponent"
                    />
                </template>
            </div>  
           </div>
        </el-row>
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

import PieChart from "./components/PieChart.vue";
import LineChart from "./components/LineChart.vue";


const dataSourceResource = new Resource("manager/source/get-all");
const totalResource = new Resource("statistic/total");
const totalPieResource = new Resource("statistic/totalPieChart");
const totalLineResource = new Resource("statistic/totalLineChart");

let today = new Date();
let day = checkZero(today.getDate() + '');
let month = checkZero((today.getMonth() + 1) + '');
let year = checkZero((today.getFullYear() + 1) + '');

export default {
  components: {
    Pagination,
    PieChart,
    LineChart,
  },
  directives: { waves },
  data() {
    return {
      renderComponent: true,
      list: null,
      dataSourceLst: null,
      total: 0,
      loading: true,
      loadingPie:true,
      optionDate:0,
      query: {
          currentPage: 1,
          rowsPerPage: 10,
          sort: "createdAt", 
          keyword: "",
          optionDate:0,
          startDate: today.getFullYear() + '-' +  month + '-' +  day + ' 00:00:00',
          endDate: getCurrentDateToStr(today),
          dataSource:null,

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

      list_pieChartData : null,
      list_ApiPieData:null,    
      pieChartData: {
                labels: [],
                datasets: [{
                  label: '',
                  backgroundColor: [],
                  data: []
                }]
      },
      pieOptions: {
        title: {
            display: false,
                  text: ''
            },
              responsive: true,
                maintainAspectRatio: false
      },
//line
    list_lineChartData:null,
    list_ApiLineData:null, 
    lineChartData: {
        labels: [],
        datasets: [
 
        ]
      },
      lineOptions: {
        title: {
          display: true,
          text: 'Biểu đồ biến động dữ liệu'
        },
        responsive: true,
        maintainAspectRatio: false
      },

    datasets: [
          {
            data: [],
            label: '',
            borderColor: ''
    }],

//
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
    this.getAllDataSource();
    this.setDateValueFirst();
    this.getPieChartData();
    this.getLineChartData();
  },

  methods: {
    
    async getList() {
      this.loading = true;
      this.query.keyword = this.query.keyword.trim();

      const { data, total } = await totalResource.listByPost(this.query);
      this.list = data;
      
      if (data) {
        this.total = total;
      }
      this.loading = false;
    }, 

    async getLineChartData(){        
      this.loadingLine = true;
      this.query.keyword = this.query.keyword.trim();
      const {data} = await totalLineResource.listByPost(this.query);
      this.list_ApiLineData = data;

      if (data) {
         if(this.list_ApiLineData.length >0){
           var indexDatasetLabel = 0;
           //set date line Chart
           var chartDataSetLabel = [];
           var chartDatasetDataIndex = []
           let background = commomColor();
           let labelsDate = [] 
           for(var i= 0; i< this.list_ApiLineData.length; i++){
              if(i == 0){
                  indexDatasetLabel = 1;
                  chartDataSetLabel[i] = this.list_ApiLineData[i].dataSourceName;
                  chartDatasetDataIndex[i] = 0;
              }else{

                if(this.list_ApiLineData[i-1].dataSource !== this.list_ApiLineData[i].dataSource){
                  chartDataSetLabel[indexDatasetLabel] = this.list_ApiLineData[i].dataSourceName;
                  chartDatasetDataIndex[indexDatasetLabel] = 0;
                  indexDatasetLabel += 1;
                }
              } 

              labelsDate[i] = this.list_ApiLineData[i].byDate;
              
           }

           const uniqueSet = new Set(labelsDate); 
           const backToArrayDate = [...uniqueSet];
          // sap xep lai date 
          backToArrayDate.sort();
          for(var j = 0 ; j< backToArrayDate.length;j++){
            backToArrayDate[j] = formatDateTimeYYYYMMDD(backToArrayDate[j]);
          }
           this.lineChartData.labels = backToArrayDate;

            
             //duyet dataSouce label 
             var dataSet = [];
             for(var j = 0 ; j< chartDataSetLabel.length;j++){
              var dataInDataset = [];

              
              for(var i= 0; i< this.list_ApiLineData.length; i++){
                  if(this.list_ApiLineData[i].dataSourceName == chartDataSetLabel[j]){
                     dataInDataset.push(this.list_ApiLineData[i].totalData);
                  }
              }
              let inputDataset = {
                data : dataInDataset,
                label : chartDataSetLabel[j],
                borderColor : background[j]
              };
              dataSet.push(inputDataset);

             }
             
             this.lineChartData.datasets = dataSet;


         }else{
           this.lineChartData.datasets = [];
           this.lineChartData.labels = [];
           
         }
        this.forceRerender()
        this.loadingLine = false
      }

    },

    async getPieChartData() {
      this.loadingPie = true
      this.query.keyword = this.query.keyword.trim();
      const { data } = await totalPieResource.listByPost(this.query);
      this.list_ApiPieData = data;
      if (data) {
        if(this.list_ApiPieData.length >0){
          let list_pieChartDataLabel = [];
          let list_pieChartDataValue = [];
          let background = commomColor();
          let list_pieChartDataBackground = [];
          let total_capacity_amount = 0;
          
          for(var i= 0; i< this.list_ApiPieData.length; i++){
            list_pieChartDataLabel.push(this.list_ApiPieData[i].dataSource);
            list_pieChartDataValue.push(this.list_ApiPieData[i].totalData);
            total_capacity_amount +=  this.list_ApiPieData[i].totalData;
            list_pieChartDataBackground.push(background[i]);
          }
		      let labels = [];
	        let backgroundColor = [];
	        let dataset_data = this.refinedPercent(this.list_ApiPieData, total_capacity_amount, labels, backgroundColor);
          
          this.pieChartData.labels = labels;
          this.pieChartData.datasets[0].data = dataset_data;           
          this.pieChartData.datasets[0].backgroundColor = backgroundColor;
          this.pieOptions.title.display = true;
          this.pieOptions.title.text = 'Nguồn dữ liệu';
  
        }else{
          this.list_pieChartData = null;
        }
        this.forceRerender()  
        
      }
  
      this.loadingPie = false  
      
      
    },
    refinedPercent(data,total_capacity_amount, lables, backgroundColor){
      console.log("refinedPercent")
      console.log(data)
      let arr_data = [];
      let color = commomColor();
      let k = 0;
      data.forEach(element => {
        let percent = ((element.totalData/total_capacity_amount) * 100).toFixed(2);
        backgroundColor.push(color[k]);
        lables.push(element.dataSourceName + ": " +commonFormatBytesToSize(element.totalData) + "(" + percent + "%)");
        arr_data.push(percent);
        k++;
      });
      return arr_data;
    },

    async getAllDataSource() {
      const { data } = await dataSourceResource.list();
      if(data){
        this.dataSourceLst = data;
      }
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

    changeOptionDataSource(){
          console.log("changeOptionDataSource");
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
      this.getPieChartData();
      this.getLineChartData();
      this.getList();
    },
   forceRerender() {
       // Remove my-component from the DOM
       this.renderComponent = false;

       this.$nextTick(() => {
         // Add the component back in
         this.renderComponent = true;
       });
   }

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