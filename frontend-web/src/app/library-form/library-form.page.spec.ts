import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LibraryFormPage } from './library-form.page';

describe('LibraryFormPage', () => {
  let component: LibraryFormPage;
  let fixture: ComponentFixture<LibraryFormPage>;

  beforeEach(() => {
    fixture = TestBed.createComponent(LibraryFormPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
