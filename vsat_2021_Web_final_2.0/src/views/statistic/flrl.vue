<template>
  <div class="app-container">
    <h2 class="no-margin-top">Thống kê dung lượng theo chiều dữ liệu</h2>
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

          <el-table-column label="Dung lượng FL" width="100">
              <template slot-scope="scope">
                  <span>{{ scope.row.totalFL | formatCapacity }}</span>
              </template>
          </el-table-column>

          <el-table-column label="Dung lượng RL" width="100">
              <template slot-scope="scope">
                  <span>{{ scope.row.totalRL | formatCapacity }}</span>
              </template>
          </el-table-column>

          <el-table-column label="Dung lượng khác" width="100">
              <template slot-scope="scope">
                  <span>{{ scope.row.totalUndef | formatCapacity }}</span>
              </template>
          </el-table-column>

          <el-table-column label="Tổng dung lượng" width="100"> 
              <template slot-scope="scope">
                  <span>{{ scope.row.totalALL | formatCapacity }}</span>
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
         <el-row >
           <div class="scroll" style="height:1000px;overflow-x:hiden;overflow-y:auto">
              <div id="card-body" class="card-body" v-for="item in list_pieChartData" :key="item.id" v-loading="loadingPie">  
                <template>                      
                  <PieChart
                      :data="item.pieChartData"
                      :options="item.pieOptions"
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
const flrlChartistResource = new Resource("statistic/flRlChartist");

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
      loadingLine:true,      
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

//ChartPie
      list_pieChartData : null,
      list_ApiPieData:null,    
      pieChartData: {
                labels: [],
                datasets: [
                  {
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
      //   }
      // ],
      
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
    this.getPieChartData();
    this.getLineChartData();
    this.getAllDataSource();
    this.setDateValueFirst();
  },

  methods: {
    
    async getList() {
      this.loading = true;
      this.query.keyword = this.query.keyword.trim();
      
      const { data } = await flrlChartistResource.listByPost(this.query);
      
      
      if (data) {
        
        let arr_data_by_day = [];
        for (var k = 0; k < data.length; k++) {
          // sum data
          let fl_data = 0;
          let rl_data = 0;
	      	let other_data = 0;
          let all_data = 0;
          if(data[k].direction == "0"){
            other_data = data[k].fileSize;
          }else if(data[k].direction == "1"){
            fl_data = data[k].fileSize;
          } else if(data[k].direction == "2"){
            rl_data = data[k].fileSize;
          } 
          all_data = data[k].fileSize;
          //----------------start get data for table----------
          let date_time = data[k].byDate;
          let key_data = data[k].dataSource + "-" + date_time;
          console.log("k: "+ k +" , key_data : " + key_data)
          let element_by_day = {};
          let finter_data = arr_data_by_day.filter(elm => elm.key == key_data);
          if(finter_data.length > 0){           
              finter_data[0].totalFL = finter_data[0].totalFL + fl_data;
              finter_data[0].totalRL = finter_data[0].totalRL + rl_data;
              finter_data[0].totalUndef = finter_data[0].totalUndef + other_data;
              finter_data[0].totalALL = finter_data[0].totalALL + all_data;
          }else {
                element_by_day = {
                        key: key_data,
                        dataSource: data[k].dataSource,
                        dataSourceName: data[k].dataSourceName,
                        totalFL: fl_data,
                        totalRL: rl_data,
                        totalUndef: other_data,
                        totalALL : all_data,
                        byDate: date_time
                };               
                arr_data_by_day.push(element_by_day);
          }
        }

        this.list = arr_data_by_day;
        this.total = arr_data_by_day.length;
      }
      this.loading = false;
    }, 
    
    async getLineChartData(){        
      this.loadingLine = true;
      this.query.keyword = this.query.keyword.trim();
      const {data} = await flrlChartistResource.listByPost(this.query);

      if (data) {
        let object_data = new Map();
	      let arr_lables = [];
        var chartDataSetLabel = [];
        let background = commomColor();
        for(var k = 0; k < data.length; k++) {
          let date_time = data[k].byDate;
          if(!arr_lables.includes(date_time)){
            arr_lables.push(date_time);
          }

          let arr_data = new Array();
          let element_day = null;
          
          if(object_data.has(data[k].dataSourceName)){
              // filter day exist
              arr_data = object_data.get(data[k].dataSourceName);
              let get_day_of_week = arr_data.filter((elm, index, arr) => {
                if(elm.id == date_time){
                    arr[index].totalALL += data[k].fileSize
                }
                return elm.id == date_time;

              });

              if(get_day_of_week.length == 0) {
                element_day = {
                  id: date_time,
                  totalALL: data[k].fileSize,
                  dataSourceName: data[k].dataSourceName
                };
                arr_data.push(element_day);
              }
              

          } else {    
            //push lan dau
            if(!chartDataSetLabel.includes(data[k].dataSourceName)){
              chartDataSetLabel.push(data[k].dataSourceName);
            }
            element_day = {
              id: date_time,
              totalALL: data[k].fileSize,
              dataSourceName : data[k].dataSourceName
            };         
            arr_data.push(element_day);
          }
          object_data.set(data[k].dataSourceName, arr_data);

        }
        this.list_ApiLineData = object_data;    
        // sap xep lai date 
        arr_lables.sort();

        for(var i=0 ; i< arr_lables.length; i++){
          arr_lables[i] = formatDateTimeYYYYMMDD(arr_lables[i]);
        }

        this.lineChartData.labels = arr_lables;
        console.log(object_data)
        console.log(chartDataSetLabel)

        var dataSet = [];
        for(var j = 0 ; j< chartDataSetLabel.length;j++){
              var dataInDataset = []; 
              for (const item of this.list_ApiLineData) {
                 for(var i=0; i< item[1].length; i++){
                    if(item[1][i].dataSourceName == chartDataSetLabel[j]){
                      dataInDataset.push(item[1][i].totalALL);
                    }
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

        this.forceRerender()
        this.loadingLine = false
      }


    },

    async getPieChartData() {
      this.loadingPie = true
      this.query.keyword = this.query.keyword.trim();
      const { data } = await flrlChartistResource.listByPost(this.query);
     
      if (data) {
        let object_data = new Map();  
        for (var k = 0; k < data.length; k++) {
          // sum data
          let fl_data = 0;
          let rl_data = 0;
	        let other_data = 0;
          let all_data = 0; 
          if(data[k].direction == "0"){
            other_data = data[k].fileSize;
          }else if(data[k].direction == "1"){
            fl_data = data[k].fileSize;
          } else if(data[k].direction == "2"){
            rl_data = data[k].fileSize;
          } 
          all_data = data[k].fileSize;  
          // --------------get data for pie-----------
          let object_element = {};
          
          if(object_data.has(data[k].dataSource)){
            object_element = object_data.get(data[k].dataSource);
            object_element.totalFL = object_element.totalFL + fl_data;
            object_element.totalRL = object_element.totalRL + rl_data;
            object_element.totalUndef = object_element.totalUndef + other_data;
            object_element.totalALL = object_element.totalFL + object_element.totalRL + object_element.totalUndef
          } else {
            object_element = {
                dataSource: data[k].dataSource,
                dataSourceName: data[k].dataSourceName,
                totalFL: fl_data,
                totalRL: rl_data,
                totalUndef: other_data,
                totalALL: all_data,
              };
          }           
          object_data.set(data[k].dataSource, object_element);  

        }
        this.list_ApiPieData = object_data;                           
      
      }
      
      if(this.list_ApiPieData.size >0){
        let list_pieChartDataData = [];
        for (const item of this.list_ApiPieData) {
            var dataset_data = this.refinedPercent(item[1].totalFL, item[1].totalRL, item[1].totalUndef);  
            let list_pieChartDataMono= {
                      pieChartData: {
                            labels: ['Dung lượng FL : ' + commonFormatBytesToSize(item[1].totalFL) + " ("+ dataset_data[0] +"%)", 'Dung lượng RL : ' + commonFormatBytesToSize(item[1].totalRL) + "("+dataset_data[1] + "%)", 'Dung lượng khác : ' + commonFormatBytesToSize(item[1].totalUndef) + "("+dataset_data[2]+"%)"],
                              datasets: [{
                                label: '',
                                backgroundColor: ['#3e95cd', '#8e5ea2', '#3cba9f'],
                                data: dataset_data
                                
                              }]
                            },
                      pieOptions: {
                              title: {
                                display: true,
                                text: item[1].dataSourceName
                              },
                              responsive: true,
                              maintainAspectRatio: false
                      } 
                    
            }
            list_pieChartDataData.push(list_pieChartDataMono);
                   
        }
        this.list_pieChartData = list_pieChartDataData;    
       
      }else{
        this.list_pieChartData = null;
      }
      this.forceRerender()  
  
      this.loadingPie = false    
      
      
    }, 

    refinedPercent(fl, rl, other){
      let total = fl + rl + other;
      let arr_data = [];
      let fl_percent = ((fl/total) * 100).toFixed(2);
      let rl_percent = ((rl/total) * 100).toFixed(2);
      let other_percent = ((other/total) * 100).toFixed(2);
      arr_data.push(fl_percent);
      arr_data.push(rl_percent);
      arr_data.push(other_percent);
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
      this.getList();
      this.getPieChartData();
      this.getLineChartData();
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