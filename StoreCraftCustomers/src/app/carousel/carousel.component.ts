import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-carousel',
  templateUrl: './carousel.component.html',
  styleUrls: ['./carousel.component.css']
})
export class CarouselComponent implements OnInit {
  public myInterval: number = 3000;
  public activeSlideIndex: number = 0;
  public noWrapSlides:boolean = false;

  constructor() { }

  ngOnInit() {
  }

  activeSlideChange(){
    console.log(this.activeSlideIndex);
  }

  public slides:Array<Object> = [
    {"image":"https://mdbootstrap.com/img/Photos/Slides/img%20(18).jpg"},
    {"image":"https://mdbootstrap.com/img/Photos/Slides/img%20(19).jpg"},
    {"image":"https://mdbootstrap.com/img/Photos/Slides/img%20(20).jpg"},
  ];
}
