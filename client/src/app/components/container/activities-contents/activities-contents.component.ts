import { Component, OnInit, ViewChild } from '@angular/core';
import { ChartComponent } from 'ng-apexcharts';

import { ChartOptions } from 'src/app/model/chart-options-model';

@Component({
  selector: 'app-activities-contents',
  templateUrl: './activities-contents.component.html',
  styleUrls: ['./activities-contents.component.css'],
})
export class ActivitiesContentsComponent implements OnInit {
  @ViewChild('chart') chart!: ChartComponent;
  chartOptions!: ChartOptions;

  constructor() {}

  ngOnInit(): void {
    this.chartOptions = {
      series: [
        {
          name: '部室利用時間',
          data: [10, 41, 35, 51, 49, 62, 69, 91, 148, 30, 45, 12],
        },
      ],
      chart: {
        height: '700px',
        type: 'area',
      },
      title: {
        text: undefined,
      },
      xaxis: {
        categories: [
          'January',
          'February',
          'March',
          'April',
          'May',
          'June',
          'July',
          'August',
          'September',
          'October',
          'November',
          'December',
        ],
      },
    };
  }
}
